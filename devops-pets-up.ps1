Write-Host "Starting Minikube..."
minikube start

Write-Host "Deploying all services with Ansible..."
cd ansible
ansible-playbook deploy-all.yml -v
cd ..

Write-Host "Waiting for pods to be ready..."
kubectl wait --for=condition=Ready pods --all --timeout=180s

Write-Host "Port-forwarding services..."
Start-Process powershell -ArgumentList 'kubectl port-forward svc/frontend 8081:80'
Start-Process powershell -ArgumentList 'kubectl port-forward svc/backend 8080:8080'
Start-Process powershell -ArgumentList 'kubectl port-forward svc/keycloak 8083:8080'
Start-Process powershell -ArgumentList 'kubectl port-forward svc/jenkins 8082:8080'
Start-Process powershell -ArgumentList 'kubectl port-forward svc/mailhog 8025:8025'

Write-Host "All set! Open:"
Write-Host "Frontend:  http://localhost:8081"
Write-Host "Backend:   http://localhost:8080"
Write-Host "Keycloak:  http://localhost:8083"
Write-Host "Jenkins:   http://localhost:8082"
Write-Host "Mailhog:   http://localhost:8025"
