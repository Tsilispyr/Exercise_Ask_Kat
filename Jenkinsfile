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

        stage('Deploy with Ansible') {
            steps {
                dir('ansible') {
                    sh 'ansible-playbook deploy.yml -v'
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed!'
        }
        success {
            echo 'Deployment successful! Backend is now running on Kubernetes.'
        }
        failure {
            echo 'Deployment failed! Check the logs for more details.'
        }
    }
}
