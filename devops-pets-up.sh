#!/bin/bash
set -e

# --------- CONFIG ---------
BACKEND_IMAGE="devops-pets-backend:latest"
FRONTEND_IMAGE="devops-pets-frontend:latest"
JENKINS_IMAGE="custom-jenkins:latest"
KEYCLOAK_CONFIGMAP="keycloak-realm-config"
KEYCLOAK_REALM_FILE="keycloak/realm-export.json"

# --------- FUNCTIONS ---------
function check_cmd() {
  command -v "$1" >/dev/null 2>&1 || { echo "[ERROR] Απαιτείται το $1 αλλά δεν βρέθηκε!"; exit 1; }
}

function build_image_if_needed() {
  local image=$1
  local dir=$2
  if docker image inspect "$image" >/dev/null 2>&1; then
    echo "[SKIP] Το image $image υπάρχει ήδη, δεν γίνεται rebuild."
  else
    echo "[BUILD] Χτίζω image $image από $dir ..."
    cd "$dir"
    docker build -t "$image" . || { echo "[ERROR] Αποτυχία build για $image!"; exit 1; }
    cd - >/dev/null
  fi
}

function create_configmap() {
  echo "[CONFIGMAP] Δημιουργία/ενημέρωση ConfigMap για Keycloak..."
  kubectl delete configmap $KEYCLOAK_CONFIGMAP --ignore-not-found
  kubectl create configmap $KEYCLOAK_CONFIGMAP --from-file=$KEYCLOAK_REALM_FILE || { echo "[ERROR] Αποτυχία δημιουργίας ConfigMap!"; exit 1; }
}

function wait_for_pods() {
  echo "[WAIT] Περιμένω όλα τα pods να γίνουν Ready..."
  kubectl wait --for=condition=Ready pods --all --timeout=300s || {
    echo "[ERROR] Κάποιο pod δεν έγινε Ready!";
    kubectl get pods -o wide
    exit 1
  }
}

function port_forward_with_retry() {
  local svc=$1
  local local_port=$2
  local remote_port=$3
  local name=$4
  local max_retries=5
  local count=1
  while [ $count -le $max_retries ]; do
    echo "[${name}] Προσπάθεια $count για port-forward..."
    kubectl port-forward svc/$svc $local_port:$remote_port &
    pf_pid=$!
    sleep 3
    if nc -z localhost $local_port; then
      echo "[${name}] Port-forward πέτυχε στη θύρα $local_port!"
      wait $pf_pid
      return 0
    else
      echo "[${name}] Αποτυχία port-forward στη θύρα $local_port."
      kill $pf_pid 2>/dev/null
      sleep 2
    fi
    count=$((count+1))
  done
  echo "[${name}] Αποτυχία μετά από $max_retries προσπάθειες."
}

function ensure_minikube_docker_env() {
  eval $(minikube -p minikube docker-env)
}

# --------- SOFT CLEANUP ---------
function k8s_soft_cleanup() {
  echo "[CLEANUP] Διαγράφω όλα τα deployments, pods, services, replicaSets, daemonSets, statefulSets, jobs, cronjobs, ingress (όχι PVC/ConfigMap/Secrets)..."
  kubectl delete deployment --all --ignore-not-found
  kubectl delete pod --all --ignore-not-found
  kubectl delete service --all --ignore-not-found
  kubectl delete replicaset --all --ignore-not-found
  kubectl delete daemonset --all --ignore-not-found
  kubectl delete statefulset --all --ignore-not-found
  kubectl delete ingress --all --ignore-not-found || true
  kubectl delete job --all --ignore-not-found || true
  kubectl delete cronjob --all --ignore-not-found || true
  echo "[CLEANUP] Ολοκληρώθηκε. Τα δεδομένα (PVC, ConfigMap, Secrets) διατηρούνται."
}

# Περιμένει μέχρι το image να είναι διαθέσιμο στο Minikube node
function wait_for_image_on_node() {
  local image=$1
  local max_retries=12
  local count=1
  echo "[WAIT] Περιμένω το image $image να γίνει διαθέσιμο στο Minikube node..."
  while [ $count -le $max_retries ]; do
    # Έλεγχος αν το image είναι διαθέσιμο στο node μέσω crictl (containerd)
    if minikube ssh "sudo crictl images | grep -q $image"; then
      echo "[WAIT] Το image $image είναι διαθέσιμο στο Minikube node!"
      return 0
    fi
    echo "[WAIT] Το image $image ΔΕΝ είναι ακόμα διαθέσιμο, retry $count/$max_retries..."
    sleep 5
    count=$((count+1))
  done
  echo "[ERROR] Το image $image ΔΕΝ έγινε διαθέσιμο στο Minikube node μετά από $((max_retries*5)) δευτερόλεπτα!"; exit 1
}

function force_build_image() {
  local image=$1
  local dir=$2
  echo "[FORCE BUILD] Διαγράφω τυχόν παλιό image $image..."
  docker rmi $image || true
  echo "[FORCE BUILD] Χτίζω νέο image $image από $dir ..."
  cd "$dir"
  docker build -t "$image" . || { echo "[ERROR] Αποτυχία build για $image!"; exit 1; }
  cd - >/dev/null
  echo "[FORCE BUILD] Φόρτωση image στο Minikube..."
  minikube image load $image
  wait_for_image_on_node $image
}

# --------- MAIN SCRIPT ---------

# Κάλεσε το cleanup στην αρχή
k8s_soft_cleanup

# 0. Έλεγχος dependencies
for cmd in docker kubectl ansible-playbook nc minikube; do check_cmd $cmd; done

# 1. Minikube docker env
ensure_minikube_docker_env

# 2. Force rebuild images (πάντα, για να είναι φρέσκα)
echo "[BUILD] Force rebuild όλων των images..."

force_build_image "$BACKEND_IMAGE" Ask
force_build_image "$FRONTEND_IMAGE" frontend
force_build_image "$JENKINS_IMAGE" k8s/jenkins

# Deploy Jenkins manifests (pvc, rbac, deployment, service)
echo "[DEPLOY] Deploy Jenkins manifests..."
kubectl apply -f k8s/jenkins/jenkins-pvc.yaml
kubectl apply -f k8s/jenkins/jenkins-rbac.yaml
kubectl apply -f k8s/jenkins/jenkins-deployment.yaml
kubectl apply -f k8s/jenkins/jenkins-service.yaml

# 3. Δημιουργία ConfigMap για Keycloak
create_configmap

# 4. Deploy όλων των services με Ansible
if [ -d ansible ]; then
  echo "[DEPLOY] Deploy όλων των services με Ansible..."
  cd ansible
  ansible-playbook deploy-all.yml -v || { echo "[ERROR] Αποτυχία Ansible deploy!"; exit 1; }
  cd ..
else
  echo "[ERROR] Δεν βρέθηκε φάκελος ansible!"; exit 1
fi

# 5. Wait for pods
wait_for_pods

# 5b. Jenkins pod ImagePullBackOff auto-fix
function fix_jenkins_imagepull() {
  local max_retries=3
  local count=1
  while [ $count -le $max_retries ]; do
    jenkins_status=$(kubectl get pods -l app=jenkins -o jsonpath='{.items[0].status.containerStatuses[0].state.waiting.reason}' 2>/dev/null || echo "")
    if [[ "$jenkins_status" == "ImagePullBackOff" || "$jenkins_status" == "ErrImagePull" ]]; then
      echo "[JENKINS] Εντοπίστηκε $jenkins_status! Αυτόματο rebuild image και διαγραφή pod (προσπάθεια $count/$max_retries)..."
      ensure_minikube_docker_env
      docker rmi $JENKINS_IMAGE || true
      build_image_if_needed "$JENKINS_IMAGE" k8s/jenkins
      kubectl delete pod -l app=jenkins
      sleep 10
    else
      echo "[JENKINS] Jenkins pod δεν έχει ImagePullBackOff. Προχωράμε."
      return 0
    fi
    count=$((count+1))
    sleep 10
  done
  # Τελικός έλεγχος
  jenkins_status=$(kubectl get pods -l app=jenkins -o jsonpath='{.items[0].status.containerStatuses[0].state.waiting.reason}' 2>/dev/null || echo "")
  if [[ "$jenkins_status" == "ImagePullBackOff" || "$jenkins_status" == "ErrImagePull" ]]; then
    echo "[ERROR] Jenkins pod παραμένει σε $jenkins_status μετά από $max_retries προσπάθειες!"; exit 1
  fi
}

fix_jenkins_imagepull

# 6. Port-forward στα βασικά services (στο background)
echo "[PORT-FWD] Κάνω port-forward στα βασικά services..."
port_forward_with_retry backend 8083 8080 "Backend" &
port_forward_with_retry mailhog 8025 8025 "Mailhog" &
port_forward_with_retry keycloak 8081 8080 "Keycloak" &
port_forward_with_retry jenkins 8082 8080 "Jenkins" &
wait

echo "\n===================================="
echo "Όλα έτοιμα! Άνοιξε:" 
echo "Backend:   http://localhost:8083"
echo "Mailhog:   http://localhost:8025"
echo "Keycloak:  http://localhost:8081"
echo "Jenkins:   http://localhost:8082"
echo "====================================" 