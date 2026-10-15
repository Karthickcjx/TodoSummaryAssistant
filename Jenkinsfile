pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        GIT_URL = 'https://github.com/Karthickcjx/TodoSummaryAssistant.git'
        GIT_BRANCH = 'main'
        IMAGE_NAME = '066107820394.dkr.ecr.ap-south-1.amazonaws.com/todo-backend'
        REGISTRY = 'https://066107820394.dkr.ecr.ap-south-1.amazonaws.com'
        REGISTRY_CREDENTIAL = 'ecr:ap-south-1:jenkins-user'
        
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: "${GIT_BRANCH}", url: "${GIT_URL}"
            }
        }

        stage('Build') {
            steps {
                dir('Backend/todo-summary-assistant') {
                    sh 'mvn clean install -DskipTests'
                }
            }
        }

        stage('Unit Test') {
            steps {
                dir('Backend/todo-summary-assistant') {
                    sh 'mvn test'
                }
            }
        }

        stage('Sonar Code Analysis') {
            environment {
                scannerHome = tool 'Sonar8.0'
            }
            steps {
                withSonarQubeEnv('sonarserver') {
                    dir('Backend/todo-summary-assistant') {
                        sh '''
                            ${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=todo-summary-assistant \
                            -Dsonar.projectName=todo-summary-assistant \
                            -Dsonar.projectVersion=1.0 \
                            -Dsonar.sources=src/main \
                            -Dsonar.tests=src/test \
                            -Dsonar.junit.reportPaths=target/surefire-reports \
                            -Dsonar.java.binaries=target/classes
                        '''
                    }
                }
            }
        }

        stage('Build App Image') {
            steps {
                script {
                    dockerImage = docker.build(
                        "${IMAGE_NAME}:${BUILD_NUMBER}",
                        "./Backend/todo-summary-assistant"
                    )
                }
            }
        }

        stage('Upload App Image') {
            steps {
                script {
                    docker.withRegistry(REGISTRY, REGISTRY_CREDENTIAL) {
                        dockerImage.push("${BUILD_NUMBER}")
                        dockerImage.push('latest')
                    }
                }
            }
        }
    }
}