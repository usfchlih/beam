pipeline {
  	//agent any
  	//TODO use different agent of each Stage
  	agent { 
  		node { 
  			label 'ec2' 
  		} 
  	}
  	options {
        timeout(time: 1, unit: 'HOURS') 
    }
	stages {

		stage('scm') {
            steps {
                checkout scm
            }
        }

	    stage('build-master') {

	    	when {
	                branch 'master'
	        }

	      steps {
	        sh './gradlew build'
	      }
	    
	    }
	    //periodicTest
	    stage('build-master-periodic') {

	    	when {
	                branch 'master'
	        }

	      steps {
	        sh './gradlew build periodicTest -PappArgs="['--config', 'test/input/sf-light/sf-light.conf']" -PmaxRAM=31g'
	      }
	    
	    }

	    stage('build-4ci'){
	    	 when {
	                branch 'origin/**4ci**'
	            }
	        steps {
	        sh './gradlew build'
	      }
	    }
	  
	  }
}
