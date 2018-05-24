pipeline {
  
  agent {
        node{
          label 'ec2'
        }
 
  stages {
    stage('build') {  
      }
      steps {
        checkout scm
        sh './gradlew build'
      }
    }
    stage('build-periodicTest') {
      steps {
        checkout scm
        sh './gradlew build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
      }
    }
  }
  options {
    timeout(time: 1, unit: 'HOURS')
  }
}
