pipeline {
    agent {
        label 'slave1'
    }
    tools {
        maven 'Maven-3.8.9'
        jdk 'JDk-17'
    } 
    stages {
        stage ('build'){
            steps {
                echo "building Eureka applicationda"
                sh "mvn package"
            }
        }
    }
}