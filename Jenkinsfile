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
                sh "'${mvnHome}/bin/mvn' -s $MAVEN_SETTINGS clean install -Ddocumentation.dir=${WORKSPACE}/target -Psordocs,sonatype -U"
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
       stage('\u27A1 Sonar') {
          sh "'${mvnHome}/bin/mvn' clean org.jacoco:jacoco-maven-plugin:prepare-agent verify -Dbuild.number=${BUILD_NUMBER} -Ddocumentation.dir=${WORKSPACE}/target -Pjenkins"
          sh "'${mvnHome}/bin/mvn' sonar:sonar -Pjenkins"
       }
    } finally {
        junit allowEmptyResults: true, testResults: '**/target/surefire-reports/TEST-*.xml'
    }
}
