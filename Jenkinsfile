pipeline {
    agent any

    tools {
        nodejs 'nodejs'
    }

    environment {
        IDONCARE_FRONTEND_IMAGE = 'idoncare-frontend'
    }

    stages {
        stage ('GitLab Repository Checkout') {
            steps {
                git branch: 'frontend',
                    credentialsId: 'idoncare',
                    url: 'https://lab.ssafy.com/s11-fintech-finance-sub1/S11P21A603.git'
            }
        }

        stage('Inject .env File') {
            steps {
                script {
                    echo '********** Injecting .env File **********'
                    pwd
                    configFileProvider([configFile(fileId: '934e8ea7-6b7e-4f52-9ce8-c96fdc15d2a8', targetLocation: 'frontend/.env')]) {
                        echo 'Injected .env file into frontend directory'
                    }
                }
            }
        }

        stage('Frontend Build') {
            steps {
                script {
                    echo '********** Frontend Build Start **********'
                    dir('frontend') {
                        sh 'docker build -t idoncare/$IDONCARE_FRONTEND_IMAGE .'
                    }

                    echo '********** Frontend Build End **********'
                }
            }
        }


        stage('Docker Compose Up') {
            steps {
                script {
                    echo '********** Docker Compose Start **********'

                    sh 'docker compose down'
                    sh 'docker compose up -d'

                    echo '********** Docker Compose End **********'
                }
            }
        }


        stage('Delete unnecessary Docker images') {
            steps {
                script {
                    echo '********** Delete unnecessary Docker images Start **********'

                    sh 'docker image prune -a -f'

                    echo '********** Delete unnecessary Docker images End **********'
                }
            }
        }
    }
    post {
        success {
            script {
                def Author_ID = sh(script: "git show -s --pretty=%an", returnStdout: true).trim()
                def Author_Name = sh(script: "git show -s --pretty=%ae", returnStdout: true).trim()
                mattermostSend (color: 'good',
                message: "빌드 성공: ${env.JOB_NAME} #${env.BUILD_NUMBER} by ${Author_ID}(${Author_Name})\n(<${env.BUILD_URL}|Details>)",
                endpoint: 'https://meeting.ssafy.com/hooks/xqdhpq8k6pyp9qqinegcifqdah',
                channel: 'idoncare-build-result'
                )
            }
        }
        failure {
            script {
                def Author_ID = sh(script: "git show -s --pretty=%an", returnStdout: true).trim()
                def Author_Name = sh(script: "git show -s --pretty=%ae", returnStdout: true).trim()
                mattermostSend (color: 'danger',
                message: "빌드 실패: ${env.JOB_NAME} #${env.BUILD_NUMBER} by ${Author_ID}(${Author_Name})\n(<${env.BUILD_URL}|Details>)",
                endpoint: 'https://meeting.ssafy.com/hooks/xqdhpq8k6pyp9qqinegcifqdah',
                channel: 'idoncare-build-result'
                )
            }
        }
    }
}
