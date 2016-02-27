/*
    Defines the standard continuous delivery pipeline for microservice components
    at Jenkins, Inc.
*/

def call() {
    node {
        checkout scm
        
        stage 'Build'
        sh "${tool 'Maven 3.x'}/bin/mvn install"
        
        def repoName = scm.userRemoteConfigs[0].url.split('(/jenkins-demo/|.git$)')[1]   // extract repository name
        def branch = env.BRANCH_NAME
        
        if (branch in ['dev','staging','prod']) {
            stage 'Deploy'
            sh "heroku deploy:war --war target/*.war --app ${repoName}-${branch}"
        }
    }
}
