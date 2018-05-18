pipeline {
  agent any
  stages {
    stage('build-master') {
      agent {
        node {
          label 'ec2'
        }
        
      }
      when {
        branch 'master'
      }
      steps {
        git(url: 'https://github.com/usfchlih/beam', branch: 'master', poll: true, changelog: true, credentialsId: 'usfchlih')
        sh './gradlew build'
      }
    }
  }
  options {
    timeout(time: 1, unit: 'HOURS')
  }
}