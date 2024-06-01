Sure, here's the full README for the OneP Project:

---

# OneP Project

OneP Project is a Java application designed to manage user authentication and task management for a hypothetical organization. It includes features such as user login, password recovery, and task assignment.

## Features

- **User Authentication**: Users can log in using their username and password. Different types of users, such as agents and superiors, have access to different functionalities based on their roles.
- **Password Recovery**: Users can recover their passwords by providing their username, email, and phone number. A recovery code is generated and sent to the user to reset their password.
- **Task Management**: Superiors can assign tasks to agents and monitor their progress. Agents can view assigned tasks and update their status.

## Requirements

- Java Development Kit (JDK) 8 or higher
- MySQL Database Server
- JDBC Driver for MySQL

## Installation

1. Clone the repository:

   ```bash
   git clone https://github.com/your_username/onep-project.git
   ```

2. Configure the database connection details.

Here's the full SQL code for creating the database and tables for the OneP Project:

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

3. Import the project into your preferred Java IDE.

4. Run the application.

## Usage

1. Launch the application.

2. Log in using your username and password.

3. Navigate through the application to access different features based on your role.

## Internship Information

This project is part of an internship at OneP in Al Hoceima, Morocco.

## Our Supervisor

- [Ahmed Akkar](https://github.com/Ahmed-Akkar)

## Contributors

- [Sohail Charef](https://github.com/SohailPro12)
- [Ahmed Azagaz](https://github.com/ahmedazagaz)
- [Ayoub Bakhat](https://github.com/AYOUBBAKHAT)

---
