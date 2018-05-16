pipeline {
  agent any
  
  if(env.BRANCH_NAME == 'master') {
    stage 'Only on master'
    println 'This happens only on master'
    //TODO master build

  }  else {

  stage '4ci'
    println "Current branch ${env.BRANCH_NAME}"
    //TODO branches build

 }

}
