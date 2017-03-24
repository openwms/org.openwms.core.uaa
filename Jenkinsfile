#!/usr/bin/env groovy

import hudson.model.*
import hudson.EnvVars
import groovy.json.JsonSlurperClassic
import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import java.net.URL

node {
    try {
       def mvnHome
       stage('\u27A1 Preparation') {
          git 'git@github.com:openwms/org.openwms.core.uaa.git'
          mvnHome = tool 'M3'
       }
       stage('\u27A1 Build & Deploy') {
          configFileProvider(
              [configFile(fileId: 'maven-local-settings', variable: 'MAVEN_SETTINGS')]) {
                sh "'${mvnHome}/bin/mvn' -s $MAVEN_SETTINGS clean deploy -Ddocumentation.dir=${WORKSPACE} -Dverbose=false -Psordocs,sonatype -U"
          }
       }
       stage('\u27A1 Results') {
          archive '**/target/*.jar'
          //nexusArtifactUploader credentialsId: 'sonatype-nexus-snapshots', groupId: 'org.openwms.core', nexusUrl: 'oss.sonatype.org/content/repositories/snapshots', nexusVersion: 'nexus3', protocol: 'https', repository: 'sonatype-nexus-staging', version: '1.1.0-SNAPSHOT'
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
       stage('\u27A1 Sonar') {
          sh "'${mvnHome}/bin/mvn' clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Djacoco.propertyName=jacocoArgLine -Dbuild.number=${BUILD_NUMBER} -Dbuild.date=${BUILD_ID} -Ddocumentation.dir=${WORKSPACE} -Pjenkins"
          sh "'${mvnHome}/bin/mvn' sonar:sonar -Pjenkins"
       }
    } finally {
        junit '**/target/surefire-reports/TEST-*.xml'
    }
}
