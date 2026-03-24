# Application de Gestion de Forage d'Eau

Une application Spring Boot pour la gestion des demandes de forage d'eau potable, développée avec Java 17, Spring Boot, Thymeleaf et PostgreSQL.

## 🚀 Fonctionnalités

### CRUD Complet

#### Client
- ✅ Créer un client (nom, contact)
- ✅ Lister tous les clients
- ✅ Modifier un client existant
- ✅ Supprimer un client
- ✅ Rechercher des clients par nom

#### Demande
- ✅ Créer une demande (date, lieu, district, client)
- ✅ Lister toutes les demandes
- ✅ Modifier une demande existante
- ✅ Supprimer une demande
- ✅ Rechercher des demandes par lieu, district ou période de dates
- ✅ Filtrer les demandes par client

## 🛠️ Technologies Utilisées

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Data JPA**
- **PostgreSQL**
- **Thymeleaf**
- **Bootstrap 5.3.0**
- **Maven**

## 📋 Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur
- PostgreSQL 12 ou supérieur

## 🗄️ Configuration de la Base de Données

1. Créez une base de données PostgreSQL :
   ```sql
   CREATE DATABASE forage_db;
   ```

2. Mettez à jour les informations de connexion dans `src/main/resources/application.properties` :
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/forage_db
   spring.datasource.username=votre_utilisateur
   spring.datasource.password=votre_mot_de_passe
   ```

## 🚀 Lancement de l'Application

1. Clonez le projet :
   ```bash
   git clone <repository-url>
   cd forage
   ```

2. Compilez et lancez l'application :
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. Accédez à l'application :
   ```
   http://localhost:8080
   ```

## 🌐 Routes Disponibles

### Client
- `GET /clients` - Liste des clients
- `GET /clients/new` - Formulaire d'ajout
- `POST /clients/save` - Ajouter un client
- `GET /clients/edit/{id}` - Formulaire de modification
- `POST /clients/update` - Mettre à jour un client
- `GET /clients/delete/{id}` - Supprimer un client
- `GET /clients/search?nom=xxx` - Rechercher par nom

### Demande
- `GET /demandes` - Liste des demandes
- `GET /demandes/new` - Formulaire d'ajout
- `POST /demandes/save` - Ajouter une demande
- `GET /demandes/edit/{id}` - Formulaire de modification
- `POST /demandes/update` - Mettre à jour une demande
- `GET /demandes/delete/{id}` - Supprimer une demande
- `GET /demandes/search/lieu?lieu=xxx` - Rechercher par lieu
- `GET /demandes/search/district?district=xxx` - Rechercher par district
- `GET /demandes/search/date?startDate=xxx&endDate=xxx` - Rechercher par période
- `GET /demandes/client/{id}` - Demandes d'un client spécifique

## 🎨 Interface Utilisateur

L'application utilise un thème sombre avec la palette de couleurs suivante :
- **Noir** : Arrière-plan principal
- **Bleu** : Boutons principaux et liens
- **Blanc** : Textes
- **Orange/Jaune** : Boutons de modification
- **Rouge** : Boutons de suppression

## 📊 Structure du Projet

```
src/main/java/com/example/forage/
├── controller/          # Contrôleurs MVC
│   ├── ClientController.java
│   └── DemandeController.java
├── model/              # Entités JPA
│   ├── Client.java
│   └── Demande.java
├── repository/         # Repositories Spring Data
│   ├── ClientRepository.java
│   └── DemandeRepository.java
├── service/           # Services métier
│   ├── ClientService.java
│   └── DemandeService.java
└── ForageApplication.java # Classe principale

src/main/resources/
├── templates/         # Templates Thymeleaf
│   ├── client/
│   │   ├── list.html
│   │   └── form.html
│   └── demande/
│       ├── list.html
│       └── form.html
└── application.properties # Configuration
```

## 🔧 Validation

Les formulaires incluent une validation complète :
- Champs obligatoires
- Limites de longueur
- Unicité du contact client
- Validation des dates

## 📈 Fonctionnalités Futures

- Gestion des devis
- Suivi des états des demandes
- Statistiques avancées
- Export des données
- Gestion des utilisateurs et rôles

## 🤝 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir un Pull Request

## 📄 Licence

Ce projet est sous licence MIT.
