
# 🚀 Guide de Mise en Œuvre : Pipeline CI/CD pour l'Application de Vote

Ce projet démontre la transformation d'une application monolithique "Spaghetti" en une architecture modulaire, testée et intégrée dans une chaîne d'intégration continue industrielle.

## 🛠 Pré-requis

* **Environnement :** Docker & Docker Desktop
* **Développement :** Java 17+, Maven 3.x, Git
* **Infrastructure :** Conteneurs Docker pour Jenkins et SonarQube

---

## 🏗 Phase I : Infrastructure avec Docker

L'infrastructure repose sur deux conteneurs Docker capables de communiquer entre eux.

### 1. Déploiement de SonarQube

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:lts

```

* **Accès :** `http://localhost:9000` (identifiants par défaut : `admin` / `admin`).
* **Configuration :** Générez un Token d'analyse dans *My Account > Security > Tokens*.

### 2. Déploiement de Jenkins

Pour permettre la communication entre les conteneurs sur une machine locale, nous utilisons l'alias `host-gateway` :

```bash
docker run -d \
  -v jenkins_home:/var/jenkins_home \
  -p 8080:8080 -p 50000:50000 \
  --add-host=host.docker.internal:host-gateway \
  --name jenkins jenkins/jenkins:lts

```

---

## ⚙️ Phase II : Configuration de l'Environnement

### 1. Configuration de SonarQube

1. Créer un projet nommé `voting-app-ci`.
2. Appliquer un **Quality Gate** (Seuil de couverture à 80% recommandé).

### 2. Configuration de Jenkins

1. **Plugins :** Installer *SonarQube Scanner*, *JaCoCo*, *JUnit* et *Pipeline*.
2. **Identifiants (Credentials) :** Ajouter le Token SonarQube sous l'ID `SONAR_TOKEN` (type: Secret Text).
3. **Global Tool Configuration :**
* Configurer un JDK nommé `JDK_17`.
* Configurer un Maven nommé `MAVEN_HOME_3`.


4. **Configuration Système :**
* Ajouter un serveur SonarQube nommé `SonarQube`.
* **URL :** `http://host.docker.internal:9000`.



---

## 📝 Phase III : Configuration du Projet

Le projet est piloté par deux fichiers de configuration situés à la racine.

### 1. sonar-project.properties

Indique au scanner les chemins vers le code et les rapports de tests.

```properties
sonar.projectKey=voting-app-ci
sonar.projectName=Spaghetti Voting App Refactored
sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.java.binaries=target/classes
sonar.junit.reportsPath=target/surefire-reports
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
sonar.exclusions=**/App.java

```

### 2. Jenkinsfile (Pipeline as Code)

Automatise les étapes du cycle de vie du logiciel.

```groovy
pipeline {
    agent any 
    tools {
        jdk 'JDK_17' 
        maven 'MAVEN_HOME_3' 
    }
    stages {
        stage('Checkout') {
            steps { checkout scm }
        }
        stage('Build & Test & Coverage') {
            steps { sh 'mvn clean verify' }
        }
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') { sh 'mvn sonar:sonar' }
            }
        }
        stage('Quality Gate Check') {
            steps {
                timeout(time: 18, unit: 'MINUTES') { 
                    waitForQualityGate abortPipeline: true 
                }
            }
        }
        stage('Publish Reports') {
            when { expression { return currentBuild.result == 'SUCCESS' || currentBuild.result == null } }
            steps {
                junit 'target/surefire-reports/*.xml' 
                jacoco(execPattern: 'target/jacoco.exec', classPattern: 'target/classes', sourcePattern: 'src/main/java')
            }
        }
    }
}

```

---

## 📈 Phase IV : Stratégie de Couverture

Pour garantir une métrique de qualité pertinente, nous avons exclu les classes d'interface utilisateur (CLI) du calcul de couverture :

* **Exclusion JaCoCo :** Configurée dans le `pom.xml` pour ignorer `App.class`.
* **Exclusion Sonar :** Propriété `sonar.coverage.exclusions` utilisée pour se concentrer sur la logique métier critique (Services et Stratégies).

---

## 🏁 État Final du Projet

* **Branche Master :** Stable et fusionnée.
* **Qualité SonarQube :** Grade A (Zéro vulnérabilité, Zéro bug critique).
* **Couverture de Code :** **77.6%** sur le cœur métier.
* **Pipeline :** 100% automatisé, assurant la validation du Quality Gate avant chaque déploiement potentiel.

---

**Note aux contributeurs :** Pour relancer l'environnement local après un arrêt, utilisez la commande : `docker start sonarqube jenkins`.

