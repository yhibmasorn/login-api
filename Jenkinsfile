pipeline {
    agent any

    environment {
        DOCKER_REGISTRY = 'gcr.io' // Google Container Registry domain
        PROJECT_ID = 'yhibmasorn' // Your Google Cloud project ID
        IMAGE_NAME = 'login-api'
        IMAGE_TAG = 'latest'
        REGION = 'asia-southeast1' // The region where your Cloud Run service will be deployed
        CLOUD_RUN_SERVICE = 'login-api-service' // Name of your Cloud Run service

    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from the repository
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image using the Dockerfile
                    def app = docker.build("${DOCKER_REGISTRY}/${PROJECT_ID}/${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Push Docker Image to GCR') {
            steps {
                script {
                    // Authenticate with Google Cloud and push the Docker image to Google Container Registry
                    withCredentials([file(credentialsId: 'gcp-service-account-key', variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
                        sh 'gcloud auth activate-service-account --key-file=$GOOGLE_APPLICATION_CREDENTIALS'
                        sh 'gcloud auth configure-docker ${DOCKER_REGISTRY} --quiet'
                        sh "docker push ${DOCKER_REGISTRY}/${PROJECT_ID}/${IMAGE_NAME}:${IMAGE_TAG}"
                    }
                }
            }
        }

        stage('Deploy to Cloud Run') {
            steps {
                script {
                    // Deploy the Docker image to Google Cloud Run
                    withCredentials([file(credentialsId: 'gcp-service-account-key', variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
                        sh """
                        gcloud run deploy ${CLOUD_RUN_SERVICE} \
                        --image ${DOCKER_REGISTRY}/${PROJECT_ID}/${IMAGE_NAME}:${IMAGE_TAG} \
                        --platform managed \
                        --region ${REGION} \
                        --allow-unauthenticated
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment to Cloud Run completed successfully!'
        }
        failure {
            echo 'Deployment to Cloud Run failed.'
        }
    }
}
