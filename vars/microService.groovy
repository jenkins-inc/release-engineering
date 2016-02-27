/*
    Defines the standard continuous delivery pipeline for microservice components
    at Jenkins, Inc.
*/

def call() {
    node {
        checkout scm
        
        stage 'Build'
        sh "${tool 'Maven 3.x'}/bin/mvn install"
        
        stage 'Deploy'
        def repoName = scm.userRemoteConfigs[0].url.split('(/jenkins-demo/|.git$)')[1]   // extract repository name
        
        switch (env.BRANCH_NAME) {
        case 'master':
            sh "heroku deploy:war --war target/*.war --app ${repoName}-dev"
        }
    }
}
