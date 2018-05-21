pipeline {
  
  agent {
        node {
          label 'jenkins-slave'
        }
 
  stages {
    
    stage('build') {  
      }
      steps {
        sh './gradlew build'
      }
    }
   
    stage('build-periodicTest') {
      steps {
        sh './gradlew build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
      }
    }

  }
  options {
    timeout(time: 1, unit: 'HOURS')
  }
}