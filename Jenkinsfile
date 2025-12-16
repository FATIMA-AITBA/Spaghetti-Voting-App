// Fichier: Jenkinsfile
// Placez ce fichier à la racine de votre projet Spaghetti-Voting-App/
pipeline {
    agent any 

    // 1. Outils : Doivent correspondre aux noms configurés dans Manage Jenkins -> Global Tool Configuration
    tools {
        jdk 'JDK_17' 
        maven 'MAVEN_HOME_3' 
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Fetching code from Git...'
                // Récupère le code depuis la branche qui a déclenché le build
                checkout scm 
            }
        }
        
        stage('Build & Test & Coverage') {
            steps {
                echo 'Starting Maven build, running tests, and generating JaCoCo reports...'
                // 'mvn clean verify' compile, exécute les tests JUnit et génère le rapport JaCoCo (jacoco.xml et index.html)
                sh 'mvn clean verify' 
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Starting SonarQube analysis...'
                // 'SonarQube' doit correspondre au nom du serveur configuré dans Configure System
                withSonarQubeEnv('SonarQube') { 
                    // Lance l'analyse en utilisant les propriétés définies dans sonar-project.properties
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality Gate Check') {
            steps {
                script {
                    echo 'Waiting for Quality Gate verdict from SonarQube...'
                    // Attend le résultat de SonarQube (max 5 minutes). Nécessite le plugin SonarQube Scanner.
                    timeout(time: 5, unit: 'MINUTES') { 
                        // Si le Quality Gate échoue, le pipeline s'arrête immédiatement
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }
        
        stage('Publish Reports') {
            steps {
                echo 'Publishing JUnit and JaCoCo reports in Jenkins...'
                // 1. Publie les résultats des tests JUnit (via le plugin JUnit)
                junit 'target/surefire-reports/*.xml' 
                
                // 2. Publie le rapport JaCoCo en HTML (via HTML Publisher plugin)
                publishHTML (target: [
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'target/site/jacoco', // Dossier où Maven a généré le rapport
                    reportFiles: 'index.html',
                    reportName: 'JaCoCo Coverage Report'
                ])
                // 3. Optionnel : Publie les métriques de couverture dans Jenkins (via le plugin Coverage)
                recordCoverage()
            }
        }
    }
    post {
        success {
            echo '✅ Pipeline terminé avec SUCCÈS.'
            // Optionnel : Notification ou autre action après succès
        }
        failure {
            echo '❌ Pipeline échoué: échec au Build, aux Tests, ou au Quality Gate SonarQube.'
            // Optionnel : Envoi d'un email en cas d'échec
        }
    }
}