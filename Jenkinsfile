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

        stage('Deploy to Kubernetes') {
            steps {
                echo 'Deploying to Kubernetes...'
                sh "kubectl rollout restart deployment/backend"
                sh "kubectl rollout status deployment/backend -w"
                echo 'Deployment successful!'
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed!'
        }
    }
}
