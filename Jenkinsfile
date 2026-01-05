pipeline {
    agent { label 'slave1' }

    tools {
        maven 'Maven-3.8.9'
        jdk 'JDk-17'
    }

    environment {
        APPLICATION_NAME = "eureka"
       
    }

    stages {
        stage('build') {
            steps {
                echo "building ${env.APPLICATION_NAME} applicationda"
                sh "mvn package -DskipTests=true"
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }
        stage ('sonarqube'){
            steps{
                echo " starting sonar scans"
                sh """
                mvn clean verify sonar:sonar \
                    -Dsonar.projectKey=i27-eureka2 \
                    -Dsonar.host.url=http://136.115.208.251:9000 \
                    -Dsonar.login=sqp_e2285f5a2820605e181442fa39f82e8ba6ed7387
                """
            }
        }

     }
 }

