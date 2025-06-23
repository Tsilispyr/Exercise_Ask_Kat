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

# --------- MAIN SCRIPT ---------

# 0. Έλεγχος dependencies
for cmd in docker kubectl ansible-playbook nc minikube; do check_cmd $cmd; done

# 1. Minikube docker env
echo "[ENV] Ενεργοποίηση Minikube docker-env..."
eval $(minikube -p minikube docker-env)

# 2. Build images
build_image_if_needed "$BACKEND_IMAGE" Ask
build_image_if_needed "$FRONTEND_IMAGE" frontend
build_image_if_needed "$JENKINS_IMAGE" k8s/jenkins

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