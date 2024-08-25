pipeline {
    agent any

    environment {
        PROJECT_ID = 'yhibmasorn'     // Google Cloud Project ID
        IMAGE_NAME = 'login-api'           // Name for Docker image
        REGION = 'asia-southeast1'                 // Region for Cloud Run
        CLOUD_RUN_SERVICE = 'login-api-service' // Cloud Run service name
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from the repository
                checkout scm
            }
        }

        stage('Maven Build') {
            steps {
                // Build the Maven project
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                // Use Jenkins credentials to authenticate gcloud
                withCredentials([file(credentialsId: 'gcp-service-account-key', variable: 'GOOGLE_APPLICATION_CREDENTIALS')]) {
                    // Authenticate with Google Cloud
                    sh 'gcloud auth activate-service-account --key-file=$GOOGLE_APPLICATION_CREDENTIALS'
                    
                    // Set the project
                    sh 'gcloud config set project $PROJECT_ID'
                    
                    // Build Docker image
                    sh 'docker build -t gcr.io/$PROJECT_ID/$IMAGE_NAME:$BUILD_NUMBER .'
                }
            }
        }

        stage('Push to GCR') {
            steps {
                sh 'docker push gcr.io/$PROJECT_ID/$IMAGE_NAME:$BUILD_NUMBER'
            }
        }

        stage('Deploy to Cloud Run') {
            steps {
                sh '''
                gcloud run deploy $CLOUD_RUN_SERVICE \
                --image gcr.io/$PROJECT_ID/$IMAGE_NAME:$BUILD_NUMBER \
                --platform managed \
                --region $REGION \
                --allow-unauthenticated
                '''
            }
        }
    }

    post {
        success {
            echo 'Deployment to Cloud Run successful!'
        }
        failure {
            echo 'Deployment to Cloud Run failed.'
        }
    }
}
