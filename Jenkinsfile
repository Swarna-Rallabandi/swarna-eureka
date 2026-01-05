pipeline {
    agent { label 'slave1' }

    tools {
        maven 'Maven-3.8.9'
        jdk 'JDk-17'
    }

    environment {
        APPLICATION_NAME = "eureka"
        SONAR_URL = "http://136.115.208.251:9000"
        SONAR_SERVER = "SonarQubeWH"
        POM_VERSION = readMavenPom().getVersion()
        POM_PACKAGING = readMavenPom().getPackaging()
    }

    stages {
        stage('build') {
            steps {
                echo "building ${env.APPLICATION_NAME} applicationda"
                sh "mvn package -DskipTests=true"
                archiveArtifacts artifacts: 'target/*.jar'
            }
        }

        stage('sonarqubeCodeAnalysys') {
            steps {
                echo "starting sonar scan"
                withSonarQubeEnv("${env.SONAR_SERVER}") {
                    sh """
                      mvn -X -Dsonar.verbose=true clean verify sonar:sonar \
                        -Dsonar.projectKey=i27-eureka
                    """
                }

                timeout(time: 5, unit: 'MINUTES') {
                    script {
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }

        stage('DockerBuild') {
            steps {
                echo "exsting jar format:  i27-${env.APPLICATION_NAME}-${env.POM_VERSION}.${env.POM_PACKAGING}"
                echo "new jar formati i27-${env.APPLICATION_NAME}-${BUILD_NUMBER}-${BRANCH_NAME}.${env.POM_PACKAGING}"
                echo "budiling docker image"
                echo "Building Docker image: ${env.IMAGE_NAME}:${env.POM_VERSION}"

                sh """
                  docker version
                  docker build \
                    --build-arg JAR_FILE=i27-${env.APPLICATION_NAME}-${BUILD_NUMBER}-${BRANCH_NAME}.${env.POM_PACKAGING} \
                    -t ${env.IMAGE_NAME}:${env.POM_VERSION} \
                    .
                  docker images | head -n 20
                """
            }
        }
    }
}
