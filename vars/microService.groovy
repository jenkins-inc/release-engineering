/*
    Defines the standard continuous delivery pipeline for microservice components
    at Jenkins, Inc.
*/

def call() {
    node {
        checkout scm
        sh "${tool 'Maven 3.x'}/bin/mvn install"
    }
}
