node {
    checkout scm
    if (env.BRANCH_NAME=='master')
        sh 'git push ssh://admin@localhost:50001/workflowLibs.git HEAD:master'
}
