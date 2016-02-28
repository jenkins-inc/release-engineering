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

            stage 'Test'
            sh 'sleep 300' // this is where the actual test would have gone

            stage 'Promote'
            switch (branch) {
            case 'dev':
                mergeTo('staging')
            case 'staging':
                mergeTo('prod')
            }
        }
    }
}

def mergeTo(target) {
    sh "git checkout ${target}"
    sh "git reset --hard origin/${target}" // move to the current tip
    sh "git merge ${env.BRANCH_NAME}"
}
