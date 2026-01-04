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
        SONAR_URL = "http://136.115.208.251:9000"
        SONAR_TOKEN= credentials ('sonar_creds')
    }
    stages {
        stage ('build'){
            steps {
                echo "building ${env.APPLICATION_NAME} applicationda"
                sh "mvn package -DskipTests=true"
                archiveArtifacts artifacts: 'target/*jar'
            }
        }
        stage ('sonarqubeCodeAnalysys'){
            steps {
                echo "starting sonar scan"
                withSonarQubeEnv('SonarQubeWH') {
                      sh """
             mvn clean verify sonar:sonar \
            -Dsonar.projectKey=i27-eureka \
            -Dsonar.host.url=${env.SONAR_URL}\
            -Dsonar.login=${env.SONAR_TOKEN}
                """
                }
                timeout (time: 2, unit: 'MINUTES'){
                       script{
                        waitForQualityGate abortPipeline: true
                       }
                }
               
            }
        }
        stage ('DockerBuild'){
            steps {
                echo "budiling docker image"
            }
        }
        
    }
}
 
