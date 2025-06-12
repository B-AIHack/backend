@Library('jenkins-shared-libs@master')

def sonarCredentials = 'Jenkins_User_In_Sonar'
def sonarServer = 'SonarQube-ESB'
def version
def project = 'mb-template'
def buildImage = 'nexus.sberbank.kz/repository/docker-gateway/bank/gradle:8.10.2-jdk21'
def buildRegistry = 'https://nexus.sberbank.kz/repository/docker-gateway'
def repo
def appVersion
def namespaceList = ['sb-alpha']
def branchName
def taskName
def pullRequest
def buildLibsMount = ''

pipeline {
    agent {
        node {
            label 'build_b_business'
        }
    }
    environment {
        PWD=sh(script: 'pwd', returnStdout: true).trim()
        GRADLE_OPTS="-Dmaven.repo.local=${PWD}/.m2/repository"
        GRADLE_USER_HOME="${PWD}/.gradle"
    }
    stages {
        stage('Build info') {
            steps {
                script {
                    if (env.BRANCH_NAME) {
                        branchName = env.BRANCH_NAME
                    } else {
                        branchName = env.PULL_REQUEST_BRANCH
                    }
                    pullRequest = getPullRequestData('DBO', project, env.PULL_REQUEST, branchName)
                    version = buildImageVersion().call(branchName)
                    taskName = getShortBranchName(branchName)
                    repo = getDockerRepo(branchName)
                    echo "branch: ${branchName}"
                    echo "version: ${version}"
                    if (pullRequest) {
                        echo "pr: ${pullRequest.id}"
                        echo "from: ${pullRequest.fromRef.displayId}"
                        echo "to: ${pullRequest.toRef.displayId}"
                    }
                }
            }
        }
        stage('Build') {
            agent {
                docker (
                    image: buildImage,
                    registryUrl: buildRegistry,
                    args: buildLibsMount,
                    reuseNode: true
                )
            }
            steps {
                script {
                    cache(maxCacheSize: 1000, caches: [
                      arbitraryFileCache(path: '.gradle/'),
                      arbitraryFileCache(path: '.m2/repository')
                    ]) {
                        sh("gradle --build-cache --profile build -x test")
                        sh("tar rf build_report.tar build/reports")
                        archiveArtifacts artifacts: 'build_report.tar', fingerprint: true
                        appVersion = sh(script: "gradle properties -q | grep 'version:' | awk '{print \$2}'", returnStdout: true).trim()
                        if (branchName!='master') {
                            if (branchName!='dev')
                                appVersion = appVersion + "-" + taskName
                            appVersion = appVersion + "-SNAPSHOT"
                        }
                    }
                }
            }
        }
        stage('Test') {
            agent {
                docker (
                    image: buildImage,
                    registryUrl: buildRegistry,
                    args: "${buildLibsMount} -v /run/podman/podman.sock:/var/run/docker.sock --net=host",
                    reuseNode: true
                )
            }
            steps {
                script {
                    cache(maxCacheSize: 1000, caches: [
                      arbitraryFileCache(path: '.gradle/'),
                      arbitraryFileCache(path: '.m2/repository')
                    ]) {
                        helmfileLint(namespaceList + 'digital')
                        sh(script: "gradle --profile test", returnStatus: true)
                        try {
                            sh("tar rf test_report.tar build/reports")
                            sh("tar rf test_report.tar controller/build/reports")
                            archiveArtifacts artifacts: 'test_report.tar', fingerprint: true
                        } catch (e) {
                            echo e.getMessage()
                        }
                        junit '**/build/test-results/**/*.xml'
                    }
                }
            }
        }
        stage('Sonar') {
            agent {
                docker (
                    image: buildImage,
                    registryUrl: buildRegistry,
                    args: buildLibsMount,
                    reuseNode: true
                )
            }
            steps {
                script {
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE') {
                        withCredentials([string(credentialsId: sonarCredentials, variable: 'SONAR_TOKEN')]) {
                            withSonarQubeEnv(sonarServer) {
                                cache(maxCacheSize: 250, defaultBranch: 'develop', caches: [
                                  arbitraryFileCache(path: '.gradle/'),
                                  arbitraryFileCache(path: '.m2/repository')
                                ]) {
                                    sh("gradle jacocoTestReport sonar -x test ${getSonarParams(branchName, pullRequest, version)}")
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('Publish') {
            agent {
                docker (
                    image: buildImage,
                    registryUrl: buildRegistry,
                    args: buildLibsMount,
                    reuseNode: true
                )
            }
            environment{
                IMAGE_TAG="$version"
            }
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'Nexus', usernameVariable: 'NEXUS_CREDS_USR', passwordVariable: 'NEXUS_CREDS_PSW')]) {
                        sh("gradle -Pversion=${appVersion} publish")
                        sh("gradle jib")
                    }
                }
            }
        }
        stage('Deploy') {
            agent none
            steps {
                script {
                    if (branchName=='master') {
                        build(job: "SberBusiness/cdp-micro",
                                propagate: false,
                                wait: false,
                                parameters: [
                                  string(name: 'imageTag', value: version),
                                  string(name: 'projectName', value: project)
                                ])
                        uploadGradleSbom(project)
                    } else {
                        build(job: "B-Business/cdl-micro",
                          propagate: false,
                          wait: false,
                          parameters: [
                            string(name: 'branch', value: branchName),
                            string(name: 'project', value: project)
                          ])
                    }
                }
            }
        }
        stage('Tag'){
            when {
                expression {
                    branchName=='master'
                }
            }
            steps {
                script {
                    bitbucketCreateTag("DBO", project, "refs/heads/master", appVersion)
                }
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}
