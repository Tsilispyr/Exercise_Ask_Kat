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

        stage('Build Docker Image and Load into Kind') {
            steps {
                dir('Ask') {
                    sh "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
                    sh "kind load docker-image ${IMAGE_NAME}:${IMAGE_TAG}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying application to Kubernetes...'
                sh 'kubectl apply -R -f k8s/'
                sh 'kubectl rollout restart deployment backend'
                echo 'Waiting for deployment to complete...'
                sh 'kubectl wait --for=condition=ready pod -l app=backend --timeout=120s'
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
