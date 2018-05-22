pipeline {

  agent none

  stages {
    
    stage('build') {
      agent any
      steps {
        checkout scm
        sh './gradlew clean build'
      }
    }
    stage('build-periodicTest') {
      agent any
      steps {
        sh './gradlew clean build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
      }
    }
  }
  
  options {
    timeout(time: 1, unit: 'HOURS')
  }

}