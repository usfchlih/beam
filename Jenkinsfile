pipeline {
  agent none
  stages {
    stage('build-master') {
      when {
        branch 'master'
      }
      steps {
        sh './gradlew build'
      }
    }
    stage('build-master-periodic') {
      }
      steps {
        checkout scm
        sh './gradlew build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
      }
    }
    stage('build-4ci') {
      }
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
