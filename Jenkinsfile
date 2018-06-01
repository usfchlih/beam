pipeline {
  agent { 
          node {
            label "ec2" 
          }
  }
  stages {
    stage('build') {
      when { branch "/origin/master" || branch "/origin/**4ci**" }
      steps {
        //TODO
        checkout scm
        sh './gradlew clean build'
      }
    }
    stage('build-periodicTest') {
      when { branch "origin/master" }
      steps {
        //TODO
        checkout scm
        sh './gradlew clean build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
      }
    }
  }
 options {
    timeout(time: 1, unit: 'HOURS')
  }
}
