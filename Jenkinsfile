pipeline {
  agent none
  stages {
    stage('scm') {
      agent {
        node {
          label 'ec2'
        }
        
      }
      steps {
        checkout scm
      }
    }
    stage('build-master') {
      agent {
        node {
          label 'ec2'
        }
        
      }
      when {
        branch 'master'
      }
      steps {
        sh './gradlew build'
      }
    }
    stage('build-master-periodic') {
      agent {
        node {
          label 'ec2'
        }
        
      }
      when {
        branch 'master'
      }
      steps {
        sh './gradlew build periodicTest -PappArgs="[\'--config\', \'test/input/sf-light/sf-light.conf\']" -PmaxRAM=31g'
      }
    }
    stage('build-4ci') {
      agent {
        node {
          label 'ec2'
        }
        
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