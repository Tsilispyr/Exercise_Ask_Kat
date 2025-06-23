pipeline {
    agent any

    tools {
        maven 'Maven 3.9.5'
    }

    environment {
        IMAGE_NAME = 'devops-pets-backend'
        IMAGE_TAG  = 'latest'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Java Application') {
            steps {
                dir('Ask') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('Ask') {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                }
            }
        }

        stage('Deploy Complete Application') {
            steps {
                dir('ansible') {
                    sh 'ansible-playbook deploy-all.yml -v'
                }
            }
        }

        stage('Deploy Monitoring') {
            steps {
                dir('ansible') {
                    sh 'ansible-playbook monitoring.yml -v'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed!'
        }
        success {
            echo '''
            ========================================
            DEPLOYMENT SUCCESSFUL!
            ========================================
            
            Application Components:
             ✔️ Backend Application
             ✔️PostgreSQL Database
             ✔️Keycloak Authentication
             ✔️MailHog Email Service
             ✔️Prometheus Monitoring
             Grafana Dashboards
            
            To access the application:
            kubectl port-forward service/backend 8080:8080
            
            To access monitoring:
            kubectl port-forward service/grafana 3000:3000 -n monitoring
            '''
        }
        failure {
            echo 'Deployment failed! Check the logs for more details.'
        }
    }
}
