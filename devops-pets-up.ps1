Write-Host "Starting Minikube..."
minikube start

Write-Host "Deploying all services with Ansible..."
cd ansible
ansible-playbook deploy-all.yml -v
cd ..

Write-Host "Waiting for pods to be ready..."
kubectl wait --for=condition=Ready pods --all --timeout=180s

Write-Host "Port-forwarding services..."
Start-Process powershell -ArgumentList 'kubectl port-forward svc/backend 8083:8080'
Start-Process powershell -ArgumentList 'kubectl port-forward svc/mailhog 8025:8025'
Start-Process powershell -ArgumentList 'kubectl port-forward svc/keycloak 8081:8080'

Write-Host "All set! Open:"
Write-Host "Backend:   http://localhost:8083"
Write-Host "Mailhog:   http://localhost:8025"
Write-Host "Keycloak:  http://localhost:8081" 