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

        stage('Build & Load Backend Image') {
            steps {
                dir('Ask') {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                    sh "kind load docker-image ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Build & Load Frontend Image') {
            steps {
                dir('frontend') {
                    sh "docker build -t devops-pets-frontend:latest ."
                    sh "kind load docker-image devops-pets-frontend:latest"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying application to Kubernetes...'
                sh 'kubectl apply -R -f k8s/'
                sh 'kubectl rollout restart deployment backend'
                sh 'kubectl rollout restart deployment frontend'
                echo 'Waiting for deployments to complete...'
                sh 'kubectl wait --for=condition=available --timeout=120s deployment/backend'
                sh 'kubectl wait --for=condition=available --timeout=120s deployment/frontend'
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
            
            The backend application has been successfully built and deployed.
            All services should be running in the Kubernetes cluster.
            '''
        }
        failure {
            echo 'Deployment failed! Check the logs for more details.'
        }
    }
}
