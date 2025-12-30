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
                // 'mvn clean verify' compile, exécute les tests JUnit et génère le rapport JaCoCo
                sh 'mvn clean verify' 
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                echo 'Starting SonarQube analysis...'
                // 'SonarQube' doit correspondre au nom du serveur configuré dans Configure System
                withSonarQubeEnv('SonarQube') { 
                    // Lance l'analyse en utilisant les propriétés définies dans pom.xml (y compris le chemin JaCoCo)
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality Gate Check') {
            steps {
                script {
                    echo 'Waiting for Quality Gate verdict from SonarQube...'
                    // Attend le résultat de SonarQube (max 18 minutes, suite à notre correction)
                    timeout(time: 18, unit: 'MINUTES') { 
                        // Attend et publie le statut. Si le Quality Gate échoue, le pipeline s'arrête.
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }
        
        // NOUVELLE ÉTAPE: PUBLICATION DES RAPPORTS APRÈS LA VÉRIFICATION DU QUALITY GATE
        stage('Publish Reports') {
            // Cette étape ne s'exécute que si les précédentes étapes ont réussi ou si le Quality Gate a réussi.
            when {
                expression { return currentBuild.result == 'SUCCESS' || currentBuild.result == null }
            }
            steps {
                script {
                    echo 'Publishing JUnit and JaCoCo reports in Jenkins for visualization...'

                    // 1. Publie les résultats des tests JUnit (Ajoute le lien "Test Result" au build)
                    junit 'target/surefire-reports/*.xml' 
                    
                    // 2. Publie le rapport JaCoCo en HTML via le plugin JaCoCo (recommandé)
                    // Cela crée un lien "Code Coverage Report" dans le résumé du build.
                    // Assurez-vous que le plugin JaCoCo est installé dans Jenkins.
                    jacoco(
                        execPattern: 'target/jacoco.exec', 
                        classPattern: 'target/classes', 
                        sourcePattern: 'src/main/java'
                        // L'exclusion de App.class est gérée par le pom.xml
                    )
                }
            }
        }
    }
    
    // Actions exécutées après tous les stages
    post {
        always {
            // Nettoyer les fichiers temporaires et les dépendances après le build
            sh 'mvn clean' 
            echo 'Pipeline finished.'
        }
        success {
            echo '✅ Pipeline terminé avec SUCCÈS.'
        }
        failure {
            echo '❌ Pipeline échoué: échec au Build, aux Tests, ou au Quality Gate SonarQube. Vérifiez les logs.'
        }
    }
}