pipeline {
  agent any
  stages {
    stage('build-master') {
      agent {
        node {
          label 'ec2'
        }
        
      }
      //when {
        //branch 'master'
      //}
      steps {
        git(url: 'https://github.com/usfchlih/beam', branch: 'master', poll: true, changelog: true, credentialsId: 'usfchlih')
        sh './gradlew build'
      }
    }
    stage('build-master-periodic') {
      agent {
        node {
          label 'ec2'
        }
        
      }
     // when {
       // branch 'master'
      //}
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
      //when {
        //branch 'origin/**4ci**'
      //}
      steps {
        sh './gradlew build'
      }
    }
  }
  options {
    timeout(time: 1, unit: 'HOURS')
  }
}