pipeline {
  
  agent none

  stages {
    agent { label 'ec2' }
    stage('build') {
      steps {
        sh './gradlew build'
      }
    }
  
    stage('build-periodicTest') {
      agent { label 'ec2' }
      steps {
        sh './gradlew build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
      }
    }
  }
  
  options {
    timeout(time: 1, unit: 'HOURS')
  }

}