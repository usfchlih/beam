pipeline {

  agent any

  stages {
    stage('build') {
      steps {
        checkout scm
        sh './gradlew clean build'
      }
    }
  
    stage('build-periodicTest') {
      steps {
        sh './gradlew clean build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
      }
    }
  }
  
  options {
    timeout(time: 1, unit: 'HOURS')
  }

}