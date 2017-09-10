pipeline {
   agent any
   tools {
      maven 'maven3.5.0'
      jdk 'jdk8'
   }
   stages {
      stage ('Initialization') {
          steps {
              echo 'Preparing for build'
          }
      }
      
      stage ('Build') {
          steps {
              sh 'mvn clean install' 
              archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
          }
          post {
              failure {
                  mail to: 'mep_eisen@web.de', subject: 'build of MinigamesLib failed', body: 'build of MinigamesLib failed'
              }
          }
      }
      
      stage ('Deploy') {
          when {
              expression {
                  currentBuild.result == null || currentBuild.result == 'SUCCESS'
              }
          }
          steps {
              sh 'mvn -Deisenschmiede.deployment=true -Dmaven.test.skip=true -DskipTests deploy'
          }
          post {
              failure {
                  mail to: 'mep_eisen@web.de', subject: 'deploy of MinigamesLib failed', body: 'deploy of MinigamesLib failed'
              }
          }
      }
   }
}