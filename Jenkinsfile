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
        DOCKER_HUB = "docker.io/swarna441"   
        DOCKER_CREDS = credentials('docker_creds')
        //JFROG_DOCKER_REPO = "abc.jfrog.io"
    }

    stages {
        stage('mvnBuild') {
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
        
        stage ('DockerBuildPushImage'){
            steps {
                //i27-eureka2-0.0.1-SNAPSHOT.jar
                sh """
                ls -la
                cp ${WORKSPACE}/target/i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} ./.cicd
                echo "existing jar format : i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"                          
                echo "new format : i27-${env.APPLICATION_NAME}-${BUILD_NUMBER}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                """
                script {
                      dockerBuildandPush().call()
                }
                
             }
         }

         stage ('DeploytoDev'){
            steps {
                echo "deploy top dev"
                //sh "docker run --name ${env.APPLICATION_NAME}-dev -d -p 5761:8761 -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
                //echo "it will fail now as running the same port to create container"
               script{
                    dockerDeploy('dev', '5761').call()
               } 
            }
         }
        stage ('DeploytoTest'){
            steps {
                echo "deploy top Test"
                //sh "docker run --name ${env.APPLICATION_NAME}-dev -d -p 5761:8761 -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
                //echo "it will fail now as running the same port to create container"
               script{
                    dockerDeploy('Test', '6761').call()
               } 
            }
         } 
        stage ('DeploytoStage'){
            steps {
                echo "deploy top Stage"
                //sh "docker run --name ${env.APPLICATION_NAME}-dev -d -p 5761:8761 -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
                //echo "it will fail now as running the same port to create container"
               script{
                    dockerDeploy('Stage', '7761').call()
               } 
            }
         } 
         stage ('Deploytoprod'){
            steps {
                echo "deploy top prod"
                //sh "docker run --name ${env.APPLICATION_NAME}-dev -d -p 5761:8761 -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
                //echo "it will fail now as running the same port to create container"
               script{
                    dockerDeploy('prod', '8761').call()
               } 
            }
         } 

     }
 }

def dockerBuildandPush(){
    return{
        script {
               echo "docker build image"
               sh "docker build --no-cache --build-arg JAR_PATH=i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING} -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT} ./.cicd" 
                
                echo "docker login before push to dockerhub"
                sh "docker login -u ${env.DOCKER_CREDS_USR} -p ${DOCKER_CREDS_PSW}"

                echo "docker push to dockerhub"
                sh "docker push ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
        }
    }
    
}

 def dockerDeploy(envDeploy, port){
    return {
      echo "deploying to $envDeploy"
      script {
                    try {
                      //stop the container 
                     sh "docker stop ${env.APPLICATION_NAME}-$envDeploy"
                    //remove the container 
                     sh "docker rm ${env.APPLICATION_NAME}-$envDeploy"
                    } catch (err) {
                        echo "error caught :$err"
                    }
                   
                    //create the conatiner again
                    sh "docker run --name ${env.APPLICATION_NAME}-dev -d -p $port -t ${env.DOCKER_HUB}/${env.APPLICATION_NAME}:${GIT_COMMIT}"
                }
    }
 }


//dev = 5761
//test=6761
//stage= 7761
//prod = 8761