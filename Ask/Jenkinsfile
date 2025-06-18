pipeline {
    agent any

    tools {
        maven 'Maven 3.9.5'
    }

    environment {
        IMAGE_NAME = 'devops-pets-backend'
        CONTAINER_NAME = 'backend'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/Tsilispyr/devops-pets-backend.git'
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build --no-cache -t $IMAGE_NAME .'
            }
        }

        stage('Docker Run') {
            steps {
                sh '''
                  docker stop $CONTAINER_NAME || true
                  docker rm $CONTAINER_NAME || true
                  docker run -d --name $CONTAINER_NAME \
                    --network devops-pets_default \
                    -p 8080:8080 \
                    -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/petdb \
                    -e SPRING_DATASOURCE_USERNAME=petuser \
                    -e SPRING_DATASOURCE_PASSWORD=petpass \
                    -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                    $IMAGE_NAME
                '''
            }
        }
    }
}
