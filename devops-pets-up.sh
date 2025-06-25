#!/bin/bash

# --- Dependency Checks & Installations (Linux Only) ---

function print_color {
    COLOR=$1
    MESSAGE=$2
    case $COLOR in
        "green") echo -e "\e[32m${MESSAGE}\e[0m" ;;
        "blue") echo -e "\e[34m${MESSAGE}\e[0m" ;;
        "red") echo -e "\e[31m${MESSAGE}\e[0m" ;;
        *) echo "${MESSAGE}" ;;
    esac
}

function check_and_install() {
    TOOL=$1
    INSTALL_CMD=$2
    VERSION_CMD=$3
    MIN_VERSION=$4

    if ! command -v $TOOL &> /dev/null; then
        print_color "blue" "Installing $TOOL..."
        eval "$INSTALL_CMD"
    else
        VERSION=$($VERSION_CMD | grep -oE '[0-9]+(\.[0-9]+)+')
        print_color "green" "$TOOL is already installed (version $VERSION)."
        if [ ! -z "$MIN_VERSION" ]; then
            # Version check (major.minor only)
            if [ "$(printf '%s\n' "$MIN_VERSION" "$VERSION" | sort -V | head -n1)" != "$MIN_VERSION" ]; then
                print_color "red" "$TOOL version is too old. Please update to at least $MIN_VERSION."
                exit 1
            fi
        fi
    fi
}

# Docker
check_and_install "docker" \
    "curl -fsSL https://get.docker.com | sh" \
    "docker version --format '{{.Client.Version}}' 2>/dev/null || docker --version | grep -oE '[0-9]+(\.[0-9]+)+' | head -n1" \
    "20.10"

# kubectl
check_and_install "kubectl" \
    "curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl && chmod +x kubectl && sudo mv kubectl /usr/local/bin/" \
    "kubectl version --client --output=json | grep -oE '"gitVersion": ?"v[0-9]+\.[0-9]+\.[0-9]+' | head -n1 | grep -oE '[0-9]+\.[0-9]+\.[0-9]+'" \
    "1.24"

# kind
check_and_install "kind" \
    "curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.23.0/kind-linux-amd64 && chmod +x ./kind && sudo mv ./kind /usr/local/bin/kind" \
    "kind --version | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -n1" \
    "0.20"

# Node.js
check_and_install "node" \
    "curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash - && sudo apt-get install -y nodejs" \
    "node --version | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -n1" \
    "18.0"

# npm
check_and_install "npm" \
    "curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash - && sudo apt-get install -y nodejs" \
    "npm --version | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -n1" \
    "8.0"

# Maven
check_and_install "mvn" \
    "sudo apt-get update && sudo apt-get install -y maven" \
    "mvn --version | grep -oE '[0-9]+\.[0-9]+\.[0-9]+' | head -n1" \
    "3.8"

print_color "green" "All required dependencies are installed and up to date!"

# --- Configuration ---
CLUSTER_NAME="kind"
K8S_DIR="k8s"
BACKEND_IMAGE="devops-pets-backend:latest"
FRONTEND_IMAGE="devops-pets-frontend:latest"
JENKINS_IMAGE="custom-jenkins:latest"
BACKEND_CONTEXT="./Ask"
FRONTEND_CONTEXT="./frontend"
JENKINS_CONTEXT="./k8s/jenkins"
TIMEOUT="300s" # 5 minutes

# --- Helper Functions ---
function print_color {
    COLOR=$1
    MESSAGE=$2
    case $COLOR in
        "green")
            echo -e "\n\e[32m${MESSAGE}\e[0m"
            ;;
        "blue")
            echo -e "\n\e[34m${MESSAGE}\e[0m"
            ;;
        "red")
            echo -e "\n\e[31m${MESSAGE}\e[0m"
            ;;
        *)
            echo "${MESSAGE}"
            ;;
    esac
}

# --- Main Script ---

# 1. Clean up and Create Cluster
print_color "blue" "--- Step 1: Setting up kind cluster '${CLUSTER_NAME}' ---"
if [[ $(kind get clusters | grep ${CLUSTER_NAME}) ]]; then
    print_color "green" "Cluster '${CLUSTER_NAME}' already exists. Deleting it for a clean start."
    kind delete cluster --name ${CLUSTER_NAME}
fi
print_color "green" "Creating new kind cluster with jenkins_home mount..."
# Create a kind cluster with an extra mount for Jenkins data
kind create cluster --name ${CLUSTER_NAME} --config - <<EOF
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
  extraMounts:
  - hostPath: $(pwd)/jenkins_home
    containerPath: /jenkins_data
EOF
print_color "green" "Cluster created successfully."

# 2. Build and Load Docker Images
print_color "blue" "--- Step 2: Building and Loading Docker Images ---"
print_color "green" "Building backend image: ${BACKEND_IMAGE}"
docker build -t ${BACKEND_IMAGE} ${BACKEND_CONTEXT}

print_color "green" "Building frontend image: ${FRONTEND_IMAGE}"
docker build -t ${FRONTEND_IMAGE} ${FRONTEND_CONTEXT}

print_color "green" "Building Jenkins image: ${JENKINS_IMAGE}"
docker build -t ${JENKINS_IMAGE} ${JENKINS_CONTEXT}

print_color "green" "Loading images into kind cluster..."
kind load docker-image ${BACKEND_IMAGE}
kind load docker-image ${FRONTEND_IMAGE}
kind load docker-image ${JENKINS_IMAGE}
print_color "green" "Images loaded successfully."

# 3. Apply Base Configurations (Secrets and ConfigMaps)
print_color "blue" "--- Step 3: Applying Base Configurations (Secrets and ConfigMaps) ---"
kubectl apply -f ${K8S_DIR}/postgres/postgres-secret.yaml
kubectl create configmap keycloak-realm-config --from-file=realm-export.json=keycloak/realm-export.json --dry-run=client -o yaml | kubectl apply -f -
print_color "green" "Base configurations applied."

# 4. Deploy Core Infrastructure (PostgreSQL) and Wait
print_color "blue" "--- Step 4: Deploying Core Infrastructure (PostgreSQL and Jenkins PVC) ---"
kubectl apply -f ${K8S_DIR}/postgres/postgres-pvc.yaml
kubectl apply -f ${K8S_DIR}/jenkins/jenkins-pvc.yaml
kubectl apply -f ${K8S_DIR}/postgres/postgres-deployment.yaml
kubectl apply -f ${K8S_DIR}/postgres/postgres-service.yaml

print_color "green" "Waiting for PostgreSQL to be ready..."
kubectl wait --for=condition=available --timeout=${TIMEOUT} deployment/postgres
print_color "green" "PostgreSQL is ready."

# 5. Deploy Main Applications (Keycloak, Backend, etc.) and Wait
print_color "blue" "--- Step 5: Deploying Main Applications ---"
# Apply all remaining manifests recursively, excluding what's already applied
kubectl apply -R -f ${K8S_DIR}/

print_color "green" "Waiting for all deployments to be ready..."
kubectl wait --for=condition=available --timeout=${TIMEOUT} deployment/keycloak
kubectl wait --for=condition=available --timeout=${TIMEOUT} deployment/backend
kubectl wait --for=condition=available --timeout=${TIMEOUT} deployment/frontend
kubectl wait --for=condition=available --timeout=${TIMEOUT} deployment/jenkins
kubectl wait --for=condition=available --timeout=${TIMEOUT} deployment/mailhog

print_color "green" "\n DEPLOYMENT COMPLETE! "
print_color "green" "All services are up and running."

# Step 6: Starting Port Forwarding in the Background
print_color "blue" "\n--- Step 6: Starting Port Forwarding in the Background ---"
print_color "green" "Starting port-forwarding for all services..."

# Start each port-forward in the background
kubectl port-forward svc/frontend 8081:80 &
kubectl port-forward svc/backend 8080:8080 &
kubectl port-forward svc/keycloak 8083:8080 &
kubectl port-forward svc/jenkins 8082:8080 &
kubectl port-forward svc/mailhog 8025:8025 &

echo -e "\n--- Access URLs ---"
echo "Frontend: http://localhost:8081"
echo "Backend API: http://localhost:8080"
echo "Keycloak: http://localhost:8083"
echo "Jenkins: http://localhost:8082"
echo "Mailhog: http://localhost:8025"
echo -e "\n\e[33mPort-forwarding is active in the background.\e[0m"
echo -e "\e[33mPress Ctrl+C in this terminal to stop all services.\e[0m\n"

# Wait indefinitely until the user presses Ctrl+C
wait 