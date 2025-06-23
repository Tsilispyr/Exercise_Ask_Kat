# DevOps Pets - Complete Installation Guide

## 📋 Overview

This guide provides step-by-step instructions to set up and run the complete DevOps-Pets application, including all components and infrastructure.

## 🎯 Application Components

### Core Application
- **Backend**: Spring Boot REST API (Java 17)
- **Database**: PostgreSQL
- **Authentication**: Keycloak (OAuth2/JWT)
- **Email Service**: MailHog

### Infrastructure
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **CI/CD**: Jenkins + Ansible
- **Monitoring**: Prometheus + Grafana

## 🚀 Quick Start (Production Ready)

### Prerequisites

#### 1. **Kubernetes Cluster**
```bash
# Option A: Local Development (minikube)
minikube start

# Option B: Cloud Cluster (GKE, AKS, EKS)
# Follow cloud provider documentation

# Option C: Docker Desktop Kubernetes
# Enable Kubernetes in Docker Desktop settings
```

#### 2. **kubectl**
```bash
# Download and install kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x kubectl
sudo mv kubectl /usr/local/bin/
```

#### 3. **Docker**
```bash
# Install Docker (if not using Docker Desktop)
sudo apt-get update
sudo apt-get install -y docker.io
sudo usermod -aG docker $USER
```

#### 4. **Ansible**
```bash
# Install Ansible
sudo apt-get update
sudo apt-get install -y ansible
```

#### 5. **Java & Maven** (for local development)
```bash
# Install Java 17
sudo apt-get install -y openjdk-17-jdk

# Install Maven
sudo apt-get install -y maven
```

### Installation Steps

#### Step 1: Clone Repository
```bash
git clone https://github.com/Tsilispyr/Exercise_Ask_Kat.git
cd Exercise_Ask_Kat
```

#### Step 2: Deploy Complete Application
```bash
# Deploy all components
cd ansible
ansible-playbook deploy-all.yml -v
```

#### Step 3: Deploy Monitoring (Optional)
```bash
# Deploy monitoring stack
ansible-playbook monitoring.yml -v
```

#### Step 4: Access Application
```bash
# Access backend API
kubectl port-forward service/backend 8080:8080

# Access Keycloak admin
kubectl port-forward service/keycloak 8080:8080

# Access MailHog
kubectl port-forward service/mailhog 1025:1025
kubectl port-forward service/mailhog 8025:8025

# Access Grafana (if monitoring deployed)
kubectl port-forward service/grafana 3000:3000 -n monitoring
```

## 🔧 Development Setup

### Local Development (Without Kubernetes)

#### 1. **Database Setup**
```bash
# Start PostgreSQL with Docker
docker run -d --name postgres \
  -e POSTGRES_DB=petdb \
  -e POSTGRES_USER=petuser \
  -e POSTGRES_PASSWORD=petpass \
  -p 5432:5432 \
  postgres:13
```

#### 2. **Keycloak Setup**
```bash
# Start Keycloak with Docker
docker run -d --name keycloak \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -p 8080:8080 \
  quay.io/keycloak/keycloak:latest start-dev
```

#### 3. **MailHog Setup**
```bash
# Start MailHog with Docker
docker run -d --name mailhog \
  -p 1025:1025 \
  -p 8025:8025 \
  mailhog/mailhog
```

#### 4. **Backend Development**
```bash
# Build and run backend
cd Ask
mvn spring-boot:run
```

## 🏭 CI/CD Pipeline Setup

### Jenkins Setup

#### 1. **Install Jenkins**
```bash
# Using Docker
docker run -d --name jenkins \
  -p 8080:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  jenkins/jenkins:lts
```

#### 2. **Configure Jenkins Agent**
```bash
# Install required tools on Jenkins agent
sudo apt-get update
sudo apt-get install -y \
  openjdk-17-jdk \
  maven \
  docker.io \
  ansible \
  kubectl

# Add jenkins user to docker group
sudo usermod -aG docker jenkins
```

#### 3. **Configure Jenkins Pipeline**
1. Open Jenkins UI (http://localhost:8080)
2. Create new pipeline job
3. Configure Git repository
4. Set branch to `main`
5. Use Jenkinsfile from repository

### Ansible Configuration

#### 1. **Install Collections**
```bash
cd ansible
ansible-galaxy collection install -r requirements.yml
```

#### 2. **Configure Inventory**
Edit `ansible/inventory.ini` for your environment:
```ini
[local]
localhost ansible_connection=local

[k8s_cluster]
your-k8s-host ansible_connection=ssh ansible_user=your-user
```

## 📊 Monitoring Setup

### Prometheus Configuration
```yaml
# k8s/monitoring/prometheus-config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: prometheus-config
  namespace: monitoring
data:
  prometheus.yml: |
    global:
      scrape_interval: 15s
    scrape_configs:
      - job_name: 'kubernetes-pods'
        kubernetes_sd_configs:
          - role: pod
```

### Grafana Configuration
```yaml
# k8s/monitoring/grafana-config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: grafana-config
  namespace: monitoring
data:
  grafana.ini: |
    [security]
    admin_user = admin
    admin_password = admin
```

## 🔍 Troubleshooting

### Common Issues

#### 1. **Kubernetes Connection Issues**
```bash
# Check cluster status
kubectl cluster-info

# Check nodes
kubectl get nodes

# Check pods
kubectl get pods --all-namespaces
```

#### 2. **Docker Issues**
```bash
# Check Docker status
docker info

# Check Docker daemon
sudo systemctl status docker
```

#### 3. **Ansible Issues**
```bash
# Test Ansible connection
ansible localhost -m ping

# Check Ansible version
ansible --version
```

#### 4. **Application Issues**
```bash
# Check backend logs
kubectl logs deployment/backend

# Check database logs
kubectl logs deployment/postgres

# Check Keycloak logs
kubectl logs deployment/keycloak
```

### Performance Tuning

#### 1. **Resource Limits**
```yaml
# Add to deployment YAML files
resources:
  requests:
    memory: "256Mi"
    cpu: "250m"
  limits:
    memory: "512Mi"
    cpu: "500m"
```

#### 2. **Scaling**
```bash
# Scale backend
kubectl scale deployment backend --replicas=3

# Scale database (if using StatefulSet)
kubectl scale statefulset postgres --replicas=2
```

## 📝 Configuration Files

### Environment Variables
```bash
# Backend Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/petdb
SPRING_DATASOURCE_USERNAME=petuser
SPRING_DATASOURCE_PASSWORD=petpass
SPRING_MAIL_HOST=mailhog
SPRING_MAIL_PORT=1025
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://keycloak:8080/realms/petsystem
```

### Keycloak Realm
Import the realm configuration from `keycloak/realm-export.json` into Keycloak admin console.

## 🎉 Success Criteria

Your installation is successful when:

✅ **All pods are running:**
```bash
kubectl get pods
# All pods should show STATUS: Running
```

✅ **All services are available:**
```bash
kubectl get services
# All services should have CLUSTER-IP assigned
```

✅ **Backend API responds:**
```bash
curl http://localhost:8080/api/health
# Should return 200 OK
```

✅ **Keycloak admin accessible:**
```bash
# Open http://localhost:8080 in browser
# Login with admin/admin
```

✅ **MailHog accessible:**
```bash
# Open http://localhost:8025 in browser
# Should show email interface
```

## 📞 Support

For issues and questions:
1. Check the troubleshooting section
2. Review application logs
3. Check Kubernetes events: `kubectl get events`
4. Verify network connectivity between services

## 🔄 Updates and Maintenance

### Updating Application
```bash
# Pull latest changes
git pull origin main

# Redeploy
cd ansible
ansible-playbook deploy-all.yml -v
```

### Backup and Restore
```bash
# Backup database
kubectl exec deployment/postgres -- pg_dump -U petuser petdb > backup.sql

# Restore database
kubectl exec -i deployment/postgres -- psql -U petuser petdb < backup.sql
```

---

**Happy Deploying! 🚀** 

## ΔΙΟΡΘΩΣΗ POSTGRESQL VERSION CONFLICT

### Βήμα 1: Διαγραφή παλιού PVC
```bash
# Διαγραφή PostgreSQL deployment και PVC
kubectl delete deployment postgres
kubectl delete pvc postgres-pvc
```

### Βήμα 2: Δημιουργία νέου PostgreSQL deployment με version 13
```bash
# Εφαρμογή νέου deployment με PostgreSQL 13
kubectl apply -f - <<EOF
apiVersion: apps/v1
kind: Deployment
metadata:
  name: postgres
spec:
  replicas: 1
  selector:
    matchLabels:
      app: postgres
  template:
    metadata:
      labels:
        app: postgres
    spec:
      containers:
        - name: postgres
          image: postgres:13
          ports:
            - containerPort: 5432
          env:
            - name: POSTGRES_DB
              value: petdb
            - name: POSTGRES_USER
              value: petuser
            - name: POSTGRES_PASSWORD
              value: petpass
          volumeMounts:
            - name: postgres-storage
              mountPath: /var/lib/postgresql/data
      volumes:
        - name: postgres-storage
          emptyDir: {}
EOF
```

### Βήμα 3: Εφαρμογή service
```bash
# Εφαρμογή PostgreSQL service
kubectl apply -f - <<EOF
apiVersion: v1
kind: Service
metadata:
  name: postgres
spec:
  selector:
    app: postgres
  ports:
    - protocol: TCP
      port: 5432
      targetPort: 5432
EOF
```

### Βήμα 4: Έλεγχος ότι τρέχει
```bash
# Έλεγχος PostgreSQL pod
kubectl get pods | grep postgres

# Έλεγχος logs
kubectl logs deployment/postgres

# Έλεγχος service
kubectl get service postgres
```

### Βήμα 5: Restart backend
```bash
# Restart backend deployment
kubectl rollout restart deployment/backend

# Έλεγχος status
kubectl rollout status deployment/backend
```

---

**Ξεκίνα με το Βήμα 1** (διαγραφή παλιού deployment και PVC) και πες μου τι συμβαίνει! 

Θα διορθώσουμε το PostgreSQL και μετά το backend θα τρέξει κανονικά. 🚀 