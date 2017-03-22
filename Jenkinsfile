#!groovy

node {
   def mvnHome
   stage('\u27A1 Preparation') {
      git 'git@github.com:openwms/org.openwms.core.uaa.git'
      mvnHome = tool 'M3'
   }
   stage('\u27A1 Build') {
      configFileProvider(
          [configFile(fileId: 'maven-local-settings', variable: 'MAVEN_SETTINGS')]) {
            sh "'${mvnHome}/bin/mvn' -s $MAVEN_SETTINGS clean install -Ddocumentation.dir=${WORKSPACE} -Dverbose=false -Psordocs -U"
      }
   }
   stage('\u27A1 Results') {
      archive '**/target/*.jar'
   }
   stage('\u27A1 Heroku Staging') {
      sh '''
          if git remote | grep heroku > /dev/null; then
             git remote remove heroku
          fi
          git remote add heroku https://:${DGMXCH_HEROKU_API_KEY}@git.heroku.com/openwms-core-uaa.git
          git push heroku master -f
      '''
   }
   stage('\u27A1 Deploy') {
      configFileProvider(
          [configFile(fileId: 'maven-local-settings', variable: 'MAVEN_SETTINGS')]) {
            sh "'${mvnHome}/bin/mvn' -s $MAVEN_SETTINGS deploy -Ddocumentation.dir=${WORKSPACE} -Psonatype -U"
      }
   }
   stage('\u27A1 Sonar') {
      sh "'${mvnHome}/bin/mvn' clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Djacoco.propertyName=jacocoArgLine -Dbuild.number=${BUILD_NUMBER} -Dbuild.date=${BUILD_ID} -Ddocumentation.dir=${WORKSPACE} -Pjenkins"
      sh "'${mvnHome}/bin/mvn' sonar:sonar -Pjenkins"
   }
}

