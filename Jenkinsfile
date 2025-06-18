pipeline {
    agent any

    tools {
        maven 'Maven3'  // name of Maven installation in Jenkins (Manage Jenkins > Global Tool Configuration)
        jdk 'Java17'    // name of JDK installation
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/1997alon/ChatApp-Backend.git'
            }
        }
        stage('Build') {
            steps {
                bat 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
    }
}
