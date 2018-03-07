def label = "jenkins-slave-${UUID.randomUUID().toString()}"

podTemplate(label: label, containers: [
  containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
],
volumes: [
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]){
 node(label) {
    def myRepo = checkout scm
    def gitCommit = myRepo.GIT_COMMIT
    def shortGitCommit = "${gitCommit[0..10]}"
    def previousGitCommit = sh(script: "git rev-parse ${gitCommit}~", returnStdout: true)

    stage('Test') {
      try {
        sh """
          pwd
          ./gradlew test
          """
      }
      catch (exc) {
        println "Failed to test - ${currentBuild.fullDisplayName}"
        throw(exc)
      }
    }
    stage('Build') {
      sh "./gradlew build"
    }
    stage('Create Docker images') {
      container('docker') {
        withCredentials([[$class: 'UsernamePasswordMultiBinding',
          credentialsId: 'ecr',
          usernameVariable: 'ECR_USER',
          passwordVariable: 'ECR_PASSWORD']]) {
              sh """
                docker login -u ${DOCKER_HUB_USER} -p ${DOCKER_HUB_PASSWORD} https://${ECR_HOST}
                docker build -t ${ECR_HOST}/sample-service:${currentBuild.number} .
                docker push ${ECR_HOST}/sample-service:${currentBuild.number}
                """
        }
      }
    }
  }
}