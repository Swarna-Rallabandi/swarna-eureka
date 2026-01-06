pipeline {
    agent { label 'slave1' }

    tools {
        maven 'Maven-3.8.9'
        jdk 'JDk-17'
    }

    environment {
        APPLICATION_NAME = "eureka"
        EUREKA2_URL = "http://136.115.208.251:9000"
        EUREKA2_TOKEN = credentials('eureka2_token')
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
                //to make the sonarqube stage fail if there are code smells >3
                //to access 'withSonarQubeEnv' ,in UI settings---/var/lib/jenkins --------add sonarqube servers (url/token)  
                withSonarQubeEnv('SonarQube'){
                     sh """
                        mvn clean verify sonar:sonar \
                            -Dsonar.projectKey=i27-eureka2 \
                            -Dsonar.host.url=${env.EUREKA2_URL} \
                            -Dsonar.login=${env.EUREKA2_TOKEN}
                        """
            }
                timeout (time: 10, unit: 'MINUTES'){
                    script {
                        //to access this add in sonarqube/webhook (add jenkins master url/creds)
                        waitForQualityGate abortPipeline: true
                    }
                }
                    
            }
              
        }
        stage ('DockerBuild'){
            steps {
                 echo "building the Image"
            }
        }

     }
 }

