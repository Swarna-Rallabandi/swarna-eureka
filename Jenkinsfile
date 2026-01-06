pipeline {
    agent { label 'slave1' }

    tools {
        maven 'Maven-3.8.9'
        jdk 'JDk-17'
    }

    environment {
        APPLICATION_NAME = "eureka2"
        SONAR_EUREKA2_URL = "http://136.115.208.251:9000"
        SONAR_EUREKA2_TOKEN = credentials('eureka2_token')
        POM_VERSION = readMavenPom().getVersion()
        POM_PACKAGING = readMavenPom().getPackaging ()     
        //DOCKER_HUB = "docker.io/swarna441"   
        //DOCKER_CREDS = credentials('docker_token')
        //JFROG_DOCKER_REPO = "abc.jfrog.io"
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
                            -Dsonar.host.url=${env.SONAR_EUREKA2_URL} \
                            -Dsonar.login=${env.SONAR_EUREKA2_TOKEN}
                        """
            }
                timeout (time: 5, unit: 'MINUTES'){
                    script {
                        //to access this add in sonarqube/webhook (add jenkins master url/creds)
                        waitForQualityGate abortPipeline: true
                    }
                }
                    
            }
              
        }
        
        stage ('DockerBuildImage'){
            steps {
                //i27-eureka2-0.0.1-SNAPSHOT.jar
                sh """
                ls -la
                cp ${WORKSPACE}/target/i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} ./.cicd
                echo "existing jar format : i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"                          
                echo "new format : i27-${env.APPLICATION_NAME}-${BUILD_NUMBER}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                
                echo "docker build image"
                docker build --no-cache --build-arg JAR_PATH=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t ${env.APPLICATION_NAME}:${GIT_COMMIT} ./.cicd 
                
                """
             }
         }

     }
 }


                            // -Dsonar.host.url=${env.SONAR_EUREKA2_URL} \
                            // -Dsonar.login=${env.SONAR_EUREKA2_TOKEN}