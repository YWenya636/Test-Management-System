pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/YWenya636/Test-Management-System.git'
            }
        }
        stage('Build & Test') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Publish Report') {
            steps {
                jacoco(execPattern: 'target/jacoco.exec')
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
        failure {
            emailext subject: "Pipeline failed",
                     body: "Check Jenkins for details",
                     to: "team@company.com"
        }
    }
}