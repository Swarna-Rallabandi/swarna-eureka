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
        POM_VERSION = readMavenPom().getVersion()
        POM_PACKAGING = readMavenPom.getPackaging()
       
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
               // i27-eureka-0.0.1-SNAPSHOT.jar
                echo "exsting jar format:  i27-${env.APPLICATION_NAME}-${env.POM_VERSION}-${env.POM_PACKAGING}"
                //new format
                //i27-eureka-buildnumber(22)-branchname or tag(v1.0).jar
                echo "new jar formati i27-${env.APPLICATION_NAME}"
                echo "budiling docker image"
            }
        }
        
    }
}
 
