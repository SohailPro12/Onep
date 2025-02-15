# OneP Project

OneP Project est une application Java conçue pour la gestion des tâches pour l'ONEP(office national d'eau et électricité potable) au Maroc. Il comprend des fonctionnalités telles que la connexion utilisateur, la récupération de mot de passe et l’affectation des tâches.

## Caractéristiques

- **Authentification de l’utilisateur** : L’utilisateur peut se connecter en utilisant son nom d’utilisateur et son mot de passe. Différents types d’utilisateurs, tels que les agents et les supérieurs, ont accès à différentes fonctionnalités en fonction de leurs rôles.
- **Password Recovery** : Les utilisateurs peuvent récupérer leurs mots de passe en fournissant leur nom d’utilisateur, leur adresse courriel et leur numéro de téléphone. Un code de récupération est généré et envoyé à l’utilisateur pour réinitialiser son mot de passe.
- **Gestion des tâches** : les supérieurs peuvent assigner des tâches aux agents et surveiller leur progression. Les agents peuvent voir les tâches assignées et mettre à jour leur statut.

# Exigences

- Java Development Kit (JDK) 8 ou supérieur
- Serveur de base de données MySQL
- Pilote JDBC pour MySQL

## Installation

1. Cloner le dépôt :

   ```bash
   git clone https://github.com/your_username/onep-project.git
   ```

2. Configurer les détails de connexion à la base de données.

Voici le code SQL complet pour créer la base de données et les tableaux du projet OneP :

```sql
-- MySQL database creation script
CREATE DATABASE IF NOT EXISTS onep_db;

USE onep_db;

-- Table structure for `admin`
CREATE TABLE IF NOT EXISTS `admin` (
  `login` varchar(10) NOT NULL,
  `pass` varchar(50) NOT NULL,
  PRIMARY KEY (`login`)
);

-- Table structure for `agent`
CREATE TABLE IF NOT EXISTS `agent` (
  `login` varchar(10) NOT NULL,
  `pass` varchar(50) NOT NULL,
  `NomComplete` varchar(50) NOT NULL,
  `Post` varchar(50) NOT NULL,
  `Departement` varchar(50) NOT NULL,
  `email` varchar(30) DEFAULT NULL,
  `numero_tel` int(11) DEFAULT NULL,
  PRIMARY KEY (`login`)
);


-- Table structure for `commentaires`
CREATE TABLE IF NOT EXISTS `commentaires` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comment` varchar(100) NOT NULL,
  `Agent` varchar(50) NOT NULL,
  `Id_Tache` int(11) NOT NULL,
  `progression` varchar(10) DEFAULT NULL,
  `reponse` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);


-- Table structure for `department`
CREATE TABLE IF NOT EXISTS `department` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `libelle` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
);

-- Table structure for `recuperation_mp`
CREATE TABLE IF NOT EXISTS `recuperation_mp` (
  `login` varchar(10) NOT NULL,
  `email` varchar(50) NOT NULL,
  `numero_tel` varchar(20) DEFAULT NULL,
  `code` varchar(10) DEFAULT NULL
);


-- Table structure for `superieur`
CREATE TABLE IF NOT EXISTS `superieur` (
  `login` varchar(10) NOT NULL,
  `pass` varchar(50) NOT NULL,
  `NomComplete` varchar(50) NOT NULL,
  `Post` varchar(50) NOT NULL,
  `Departement` varchar(50) DEFAULT NULL,
  `email` varchar(30) DEFAULT NULL,
  `numero_tel` int(11) DEFAULT NULL,
  PRIMARY KEY (`login`)
);

-- Table structure for `tache`
CREATE TABLE IF NOT EXISTS `tache` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Titre` varchar(50) NOT NULL,
  `Description` varchar(100) NOT NULL,
  `Agent` varchar(50) NOT NULL,
  `superieur` varchar(50) NOT NULL,
  `budget` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

```

3. Importez le projet dans votre EDI Java préféré.

4. Exécuter l’application.

## Utilisation

1. Lancer l’application.

2. Connectez-vous en utilisant votre nom d’utilisateur et mot de passe.

3. Naviguez dans l’application pour accéder à différentes fonctions en fonction de votre rôle.

## Renseignements sur le stage

Ce projet fait partie d’un stage chez OneP à Al Hoceima, au Maroc.

## Notre superviseur

- [Ahmed Akkar](https://github.com/Ahmed-Akkar)

## Contributeurs

- [Sohail Charef](https://github.com/SohailPro12)
- [Ahmed Azagaz](https://github.com/ahmedazagaz)
- [Ayoub Bakhat](https://github.com/AYOUBBAKHAT)

---
