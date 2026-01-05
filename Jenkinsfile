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

    // Set a default image name if not set elsewhere
    IMAGE_NAME = "i27-eureka"
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
        sh 'echo "BRANCH_NAME=${BRANCH_NAME}" || true'
      }
    }

    stage('Build') {
      steps {
        script {
          env.POM_VERSION   = readMavenPom().getVersion()
          env.POM_PACKAGING = readMavenPom().getPackaging()
        }

        echo "Building ${env.APPLICATION_NAME} - version ${env.POM_VERSION}"
        sh 'mvn -DskipTests=true clean package'

        sh 'ls -l target/*.jar || true'
        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
      }
    }

    stage('SonarQube Analysis') {
      steps {
        echo "Starting SonarQube scan"
        withSonarQubeEnv("${env.SONAR_SERVER}") {
          sh """
            mvn -DskipTests=true sonar:sonar \
              -Dsonar.projectKey=i27-eureka
          """
        }
      }
    }

    stage('Quality Gate') {
      steps {
        // Requires SonarQube webhook to Jenkins (/sonarqube-webhook/)
        timeout(time: 5, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }

    stage('Prepare Jar for Docker') {
      steps {
        script {
          // Find the built jar (exclude *original*.jar created by Spring Boot repackage)
          def jarPath = sh(
            script: "ls -1 target/*.jar | grep -v 'original' | head -n 1",
            returnStdout: true
          ).trim()

          if (!jarPath) {
            error "No jar found in target/. Check Maven build output."
          }

          env.DOCKER_JAR_NAME = "i27-${env.APPLICATION_NAME}-${env.BUILD_NUMBER}-${env.BRANCH_NAME}.${env.POM_PACKAGING}"
          echo "Using jar: ${jarPath}"
          echo "Docker jar name: ${env.DOCKER_JAR_NAME}"

          // Copy jar to workspace root with the name Dockerfile expects
          sh "cp '${jarPath}' '${env.DOCKER_JAR_NAME}'"
          sh "ls -l '${env.DOCKER_JAR_NAME}'"
        }
      }
    }

     stage('DockerBuild') {
      steps {
        echo "Building Docker image: ${env.IMAGE_NAME}:${env.POM_VERSION}"
        sh """
          docker version
          docker build \
            --build-arg JAR_FILE=${env.DOCKER_JAR_NAME} \
            -t ${env.IMAGE_NAME}:${env.POM_VERSION} \
            .
          docker images | head -n 20
        """
      }
    }
  }

  post {
    always {
      echo "Pipeline finished: ${currentBuild.currentResult}"
      // Optional cleanup:
      // sh "rm -f '${env.DOCKER_JAR_NAME}' || true"
    }
  }
}