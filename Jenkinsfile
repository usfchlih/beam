pipeline {

  agent none

  stages {  
    
    stage('build') {
      node('ec2'){
        when { branch "/origin/master" || branch "/origin/**4ci**" }
        steps {
          checkout scm
          sh './gradlew clean build'
        }
      }
    }
    stage('build-periodicTest') {
      node('ec2'){
        when { branch "origin/master" }
        steps {
          sh './gradlew clean build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
        }
      }
    }
 
  }
  
  options {
    timeout(time: 1, unit: 'HOURS')
  }

}