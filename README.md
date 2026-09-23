# HireHub — Backend (API REST)

**Nom du projet :** HireHub — Backend (API REST de la plateforme de recrutement)

---

# 2. Présentation du projet

Ce projet est une **API REST** qui gère toute la logique métier d'une plateforme de recrutement. Elle s'adresse principalement aux **entreprises (recruteurs)** qui souhaitent diffuser des offres d'emploi et suivre les candidatures, ainsi qu'aux **candidats** qui recherchent un emploi, déposent leur CV et participent à des entretiens.

Son objectif principal est de fournir une base de données sécurisée et un point d'entrée unique pour toutes les fonctionnalités de l'application (gestion des utilisateurs, des offres, des CV, des candidatures et des entretiens).

L'API est documentée automatiquement avec **Swagger UI** et expose ses données via des routes REST commençant par `/api`.

---

# 3. Problématique

Le problème identifié est que **gérer un processus de recrutement de façon manuelle est long et dispersé** : les offres sont publiées sur plusieurs canaux, les CV arrivent par e-mail, et le suivi des candidatures et des entretiens devient rapidement illisible.

La solution proposée permet de **centraliser l'ensemble du processus au même endroit** : un backend unique qui garantit la cohérence des données et la sécurité des accès, auquel le frontend se connecte pour offrir aux recruteurs et aux candidats une interface simple et fiable.

---

# 4. Fonctionnalités principales

- Créer un compte utilisateur avec un rôle (ADMIN, RECRUTEUR ou CANDIDAT)
- Se connecter et se déconnecter grâce à une authentification par jeton JWT
- Gérer les offres d'emploi (publier, modifier, consulter, supprimer)
- Téléverser, consulter et supprimer les CV des candidats
- Suivre les candidatures et modifier leur statut
- Planifier et gérer les entretiens
- Modifier son profil et changer son mot de passe

---

# 5. Technologies utilisées

| Technologie | Utilisation dans le projet |
|-------------|----------------------------|
| Java 21 | Langage de programmation principal du backend |
| Spring Boot 4 | Framework qui permet de développer l'API REST |
| Spring Security + JWT | Authentification des utilisateurs et contrôle des accès selon les rôles |
| MySQL | Base de données relationnelle qui stocke toutes les données |
| Flyway | Outil de gestion des migrations de la base de données |
| Springdoc OpenAPI | Génération automatique de la documentation de l'API (Swagger UI) |
| Docker / Docker Compose | Conteneurisation de la base de données et du backend |
| GitHub Actions | Intégration continue (tests et déploiement automatiques) |

---

# 6. Installation et lancement

## 6.1 Prérequis

Pour utiliser ce projet, vous devez disposer de :

- Java 21 (JDK)
- Maven ou le script `mvnw` inclus dans le projet
- Git
- MySQL 8 (ou Docker pour lancer la base de données automatiquement)
- IntelliJ IDEA ou VS Code

---

## 6.2 Cloner le dépôt

```bash
git clone https://github.com/elfadiliaymen/Fil-Rouge-HireHub-Backend.git
```

---

## 6.3 Ouvrir le dossier

```bash
cd Fil-Rouge-HireHub-Backend
```

---

## 6.4 Installer les dépendances

Aucune installation manuelle n'est nécessaire : Maven télécharge automatiquement toutes les dépendances au premier lancement.

```bash
./mvnw dependency:resolve
```

Sous Windows :

```bash
.\mvnw.cmd dependency:resolve
```

---

## 6.5 Variables d'environnement

Copier le fichier `.env.example` vers `.env` :

```bash
cp .env.example .env
```


## 6.6 Lancer le projet

Avec Maven :

```bash
./mvnw spring-boot:run
```

Avec Docker (base de données + backend) :

```bash
docker compose up --build
```

---

## 6.7 Ouvrir le projet

- API : `http://localhost:8090/api`
- Documentation Swagger : `http://localhost:8090/swagger-ui.html`
- Tableau de bord d'état : `http://localhost:8090/actuator/health`

---

# 7. Captures d'écran

## Capture 1

### Titre

```
Page Swagger UI de l'API
```

### Image

```md
![Page Swagger UI de l'API](docs/captures/swagger.png)
```

### Explication

Cette capture montre la documentation automatique de l'API : chaque route REST, ses paramètres et les réponses possibles y sont listés. Elle permet de tester directement chaque endpoint depuis le navigateur.

---

## Capture 2

### Titre

```
Conteneurs Docker lancés (base de données et backend)
```

### Image

```md
![Conteneurs Docker du backend](docs/captures/docker.png)
```

### Explication

Cette capture montre le backend et la base de données MySQL démarrés avec Docker Compose, prêts à être utilisés par le frontend.

---

# 8. Contribution personnelle

Ma contribution principale a porté sur la **création de toute la partie backend** : modélisation des entités, création des repositories, des services et des contrôleurs REST.

J'ai également travaillé sur la **mise en place de la sécurité** avec l'authentification par JWT et la gestion des rôles, ainsi que sur la **configuration Docker et l'intégration continue** (tests et déploiement automatiques via GitHub Actions).

J'ai été responsable de la **base de données** (migrations Flyway et peuplement initial des données) et de la **documentation de l'API** avec Swagger.

---

# 9. Difficultés rencontrées

## Difficulté 1

### Problème rencontré

Le pipeline d'intégration continue échouait avec l'erreur `./mvnw: Permission denied` : le script Maven `mvnw` n'avait pas les droits d'exécution dans le dépôt.

### Recherches / Tests

J'ai vérifié les permissions du fichier avec `git ls-files -s mvnw` : le mode était `100644`, alors qu'un fichier exécutable doit être en `100755`.

### Solution

J'ai corrigé les permissions avec la commande `git update-index --chmod=+x mvnw`, puis j'ai validé le changement avant de le pousser dans le dépôt.

### Ce que j'ai appris

J'ai appris que sous Git, les droits d'exécution doivent être enregistrés explicitement, et qu'ils sont indispensables pour les scripts exécutés en environnement Linux (comme GitHub Actions).

### Texte final

J'ai rencontré le problème suivant : le script `mvnw` ne pouvait pas être exécuté dans la pipeline. Pour comprendre l'origine du problème, j'ai vérifié les permissions du fichier dans l'index Git. J'ai résolu le problème en forçant le bit d'exécution avec `git update-index --chmod=+x`. Cette difficulté m'a permis d'apprendre comment Git gère les droits d'exécution des fichiers.

---

## Difficulté 2

### Problème rencontré

Certaines valeurs (comme les années d'expérience dans une offre d'emploi) pouvaient être nulles et provoquer une `NullPointerException` dans les services.

### Recherches / Tests

J'ai écrit des tests unitaires avec Mockito pour reproduire ces cas, puis je me suis servi des rapports d'erreur de la console pour identifier les endroits précis concernés.

### Solution

J'ai ajouté des vérifications de nullité avec obligatoire, et j'ai adapté les tests pour que le service de l'utilisateur courant soit simulé correctement.

### Ce que j'ai appris

J'ai appris à toujours anticiper les valeurs nulles provenant de la base de données et à utiliser les tests unitaires pour détecter ces cas avant la mise en production.

---

# 10. Améliorations possibles

Dans une prochaine version, je pourrais :

- ajouter davantage de tests (intégration et sécurité des endpoints) ;
- mettre en place un système de notifications par e-mail (confirmation de candidature, invitation à un entretien) ;
- ajouter la pagination et le filtrage avancé des offres et des candidatures ;
- déployer l'application sur un serveur de production avec HTTPS ;
- ajouter un mécanisme de rafraîchissement des jetons JWT.

### Conclusion

Ces améliorations permettraient de **renforcer la fiabilité et la sécurité de l'API**, tout en offrant une meilleure expérience aux recruteurs et aux candidats lorsque le nombre d'utilisateurs augmente.

---

# ✅ Checklist finale

## Présentation

- [x] Le nom du projet est clair.
- [x] Le projet est présenté en 3 à 5 lignes.
- [x] Le public cible est identifié.
- [x] Le besoin est expliqué.
- [x] L'objectif est précisé.

## Fonctionnalités

- [x] 3 à 6 fonctionnalités (7 ici).
- [x] Chaque fonctionnalité commence par un verbe.
- [x] Elles correspondent à des actions réelles.

## Technologies

- [x] Les technologies sont indiquées.
- [x] Leur rôle est expliqué.

## Installation

- [x] Les prérequis sont présents.
- [x] Le dépôt est correct.
- [x] Les commandes fonctionnent.
- [x] L'adresse locale est indiquée.
- [x] Aucune donnée sensible n'est publiée.

## Captures

- [ ] Deux captures minimum (à ajouter dans `docs/captures/`).
- [ ] Chaque capture possède un titre.
- [ ] Les images fonctionnent.

## Contribution

- [x] Ma contribution est précise.
- [x] Les tâches sont clairement décrites.
- [x] Je distingue mon travail de celui du groupe.

## Difficultés

- [x] Les difficultés sont expliquées.
- [x] Les recherches sont décrites.
- [x] Les solutions sont précisées.
- [x] Les apprentissages sont présentés.

## Améliorations

- [x] 2 à 4 améliorations (5 ici).
- [x] Elles sont réalistes.