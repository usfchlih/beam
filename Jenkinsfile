pipeline {
  agent none
  stages {
    stage('scm') {
      steps {
        checkout scm
      }
    }
    stage('build-master') {
      when {
        branch 'master'
      }
      steps {
        sh './gradlew build'
      }
    }
    stage('build-master-periodic') {
      when {
        branch 'master'
      }
      steps {
        sh './gradlew build'
      }
    }
    stage('build-4ci') {
      when {
        branch 'origin/**4ci**'
      }
      steps {
        sh './gradlew build'
      }
    }
  }
  options {
    timeout(time: 1, unit: 'HOURS')
  }
}