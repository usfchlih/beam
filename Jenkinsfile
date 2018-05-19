pipeline {
   agent {
        node {
          label 'ec2'
        }     
   }
  stages {

    stage('clone') {
     	checkout scm
    }  	

    stage('build-master') {
      steps {
        sh './gradlew build'
      }
    }
    
    stage('build-master-periodic') {
      steps {
        sh './gradlew build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
      }
    }
  }
  options {
    timeout(time: 1, unit: 'HOURS')
  }
}