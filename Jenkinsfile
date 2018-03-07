def label = "jenkins-slave-${UUID.randomUUID().toString()}"

podTemplate(label: label, containers: [
  containerTemplate(name: 'gradle', image: 'niwang/gradle:0.0.1', command: 'cat', ttyEnabled: true),
  containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true),
],
volumes: [
  hostPathVolume(mountPath: '/home/gradle/.gradle', hostPath: '/tmp/jenkins/.gradle'),
  hostPathVolume(mountPath: '/var/run/docker.sock', hostPath: '/var/run/docker.sock')
]){
 node(label) {
    def myRepo = checkout scm
    def gitCommit = myRepo.GIT_COMMIT
    def shortGitCommit = "${gitCommit[0..10]}"
    def previousGitCommit = sh(script: "git rev-parse ${gitCommit}~", returnStdout: true)

    stage('Test') {
      try {
        container('gradle') {
          sh """
            pwd
            ./gradlew test
            """
        }
      }
      catch (exc) {
        println "Failed to test - ${currentBuild.fullDisplayName}"
        throw(exc)
      }
    }
    stage('Build') {
      container('gradle') {
        sh "./gradlew build"
      }
    }
    stage('Create Docker images') {
      container('docker') {

      sh """
        docker build -t sample-service:${currentBuild.number} .
        """

      }
    }
  }
}