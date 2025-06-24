pipeline {
    agent any

    tools {
        maven 'Maven 3.9.5'
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
            The backend and frontend have been deployed.
            '''
        }
        failure {
            echo 'Deployment failed! Check the logs for more details.'
        }
    }
}
