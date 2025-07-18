pipeline {
    agent any
    environment {
        DOCKER_IMAGE = 'myeduguri383/authentication-services'
        DOCKER_TAG = 'latest'
        GITHUB_REPOSITORY_URL = 'https://github.com/myeduguri/authentication-services.git'
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                git branch: 'main', url: "${GITHUB_REPOSITORY_URL}"
            }
        }
        stage('Build') {
            steps {
                echo 'Building the application...'
                bat 'mvn clean package -DskipTests'
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                bat 'mvn test'
            }
        }
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                bat """
                docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                """
            }
        }
        stage('Deploy to Minikube') {
            steps {
                echo 'Deploying to Minikube...'
                bat """
                kubectl apply -f kubernetes/authentication-services-deployment.yaml
                kubectl apply -f kubernetes/authentication-services-service.yaml
                kubectl apply -f kubernetes/authentication-services-ingress.yaml
                echo '🧹 Deleting existing pod to force image pull...'
                kubectl delete pod -l app=authentication-services --ignore-not-found=true
                """
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Please check the logs.'
        }
    }
}