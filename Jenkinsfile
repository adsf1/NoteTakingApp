pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'be/docker-compose.yml'
        // APP_CONTAINER = 'note_taking_app_be'
    }

    tools {
        maven 'Maven-3.9.1'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/adsf1/NoteTakingApp.git'
            }
        }

        stage('Build App & Test') {
            steps {
                dir('be'){
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker-compose -f ${DOCKER_COMPOSE_FILE} build"
                }
            }
        }

        /*
        stage('Run Tests') {
            steps {
                script {
                    sh "docker-compose -p test_container -f ${DOCKER_COMPOSE_FILE} up -d"
                    sh "docker-compose -p test_container exec ${APP_CONTAINER} mvn test"
                    sh "docker-compose -p test_container down --remove-orphans"
                }
            }
        }
        */

        stage('Deploy to EC2'){
            steps {
                script {
                    sh "docker-compose -f ${DOCKER_COMPOSE_FILE} up -d"
                }
            }
        }
    }

    post {
        always {
            sh 'docker system prune -f'
        }
    }
}