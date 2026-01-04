pipeline {
    agent {
        label 'slave1'
    }
    tools {
        maven 'Maven-3.8.9'
        jdk 'JDk-17'
    } 
    environment {
        APPLICATION_NAME = "eureka"
    }
    stages {
        stage ('build'){
            steps {
                echo "building ${env.APPLICATION_NAME} applicationda"
                sh "mvn package -DskipTests=true"
                archiveArtifacts artifacts: 'target/*jar'
            }
        }
        stage ('')
    }
}