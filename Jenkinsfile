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
        EC2_USER = 'ubuntu'
        EC2_HOST = '172.31.0.119'
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
        stage('Remove Container Images'){
            steps{
                sh 'docker rmi -f $(docker images -a -q)'
            }
        }
        stage('Deploy to EC2') {
            steps {
                sshagent(['ec2-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ${EC2_USER}@${EC2_HOST} '
                            aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin 066107820394.dkr.ecr.ap-south-1.amazonaws.com
                            docker pull ${IMAGE_NAME}:${BUILD_NUMBER}
                            docker stop todo-backend || true
                            docker rm todo-backend || true
                            docker run -d --name todo-backend --restart always -p 8080:8080 ${IMAGE_NAME}:${BUILD_NUMBER}
                            docker image prune -f
                        '
                    """
                }
            }
        }
    }
}