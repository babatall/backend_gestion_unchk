# 📚 Gestion Universitaire UNCHK - Backend API

## 🎯 Vue d'ensemble

Plateforme de gestion universitaire moderne et complète pour l'Université Cheikh Anta Diop (UNCHK). Cette API fournit toutes les fonctionnalités nécessaires pour gérer les étudiants, les enseignants, les cours, les inscriptions et les évaluations.

---

## 🏗️ Architecture

### Couches de l'application
```
┌─────────────────────────────────────────────┐
│           REST Controllers                  │
│   (Endpoints API / HTTP Requests)           │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│           Service Layer                     │
│   (Logique métier / Transactions)           │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│           Repository Layer                  │
│   (Accès données / JPA)                     │
└──────────────────┬──────────────────────────┘
                   │
┌──────────────────▼──────────────────────────┐
│           MySQL Database                    │
│   (Stockage persistant)                     │
└─────────────────────────────────────────────┘
```

### Stack technologique
- **Framework**: Spring Boot 3.5.14
- **Authentification**: Spring Security + JWT
- **Base de données**: MySQL 8.0+
- **ORM**: JPA/Hibernate
- **Validation**: Jakarta Bean Validation
- **API Documentation**: Swagger/OpenAPI 3.0
- **Utilities**: Lombok
- **Build**: Maven

---

## 📋 Entités et Relations

### Diagramme ER
```
┌──────────────┐         ┌─────────────────┐
│     User     │────────▶│   RoleEntity    │
│              │ (M:N)   │                 │
└──────────────┘         └────┬────────────┘
       ▲                       │
       │                       │ (M:N)
       │          ┌────────────▼──────────┐
       │          │  PermissionEntity     │
       │          └───────────────────────┘
       │
  ┌────┴────────────────┐
  │                     │
┌─┴──────────────┐  ┌──┴──────────────┐
│    Student     │  │    Teacher      │
│                │  │                 │
└────┬───────────┘  └──┬──────────────┘
     │                 │
     │                 │
     └────┬────────────┘
          │
     ┌────▼─────────┐
     │    Course    │
     └────┬─────────┘
          │
     ┌────┴──────────┐
     │                │
┌────▼────────┐  ┌───▼──────────┐
│  Enrollment │  │    Grade     │
└─────────────┘  └──────────────┘
```

### Entités principales

| Entité | Description | Relations |
|--------|-------------|-----------|
| **User** | Utilisateur système | ↔ Role (M:N) |
| **RoleEntity** | Rôle (ADMIN, ENSEIGNANT, ETUDIANT...) | ↔ Permission (M:N) |
| **PermissionEntity** | Permission granulaire | ↔ Role (M:N) |
| **Student** | Étudiant | ↔ User (1:1), ↔ Enrollment (1:N) |
| **Teacher** | Enseignant | ↔ User (1:1), ↔ Course (1:N) |
| **Course** | Cours/Module | ↔ Teacher (N:1), ↔ Enrollment (1:N) |
| **Enrollment** | Inscription | ↔ Student (N:1), ↔ Course (N:1) |
| **Grade** | Note/Évaluation | ↔ Student (N:1), ↔ Course (N:1), ↔ Teacher (N:1) |

---

## 🔐 Authentification et Autorisation

### Flux d'authentification JWT

```
┌─────────────────┐
│   Client        │
└────────┬────────┘
         │
         │ 1. POST /api/v1/auth/login
         │    {email, password}
         ▼
┌─────────────────────────────────┐
│   AuthController                │
└────────┬────────────────────────┘
         │
         │ 2. Authentification
         ▼
┌─────────────────────────────────┐
│   AuthService + AuthManager     │
└────────┬────────────────────────┘
         │
         │ 3. Génération JWT
         ▼
┌─────────────────────────────────┐
│   JwtService                    │
│   (sign token with HS512)       │
└────────┬────────────────────────┘
         │
         │ 4. Return JWT Token
         ▼
┌─────────────────┐
│   Client        │
│   (Stocke JWT)  │
└─────────────────┘
         │
         │ 5. Prochain request
         │    Authorization: Bearer <token>
         ▼
┌─────────────────────────────────┐
│   JwtAuthenticationFilter       │
│   (Valide et extrait claims)    │
└────────┬────────────────────────┘
         │
         │ 6. Set Authentication Context
         ▼
┌─────────────────────────────────┐
│   SecurityContextHolder         │
│   (User authentifié)            │
└─────────────────────────────────┘
```

### Rôles disponibles
- **ADMIN**: Accès administrateur système
- **RECTEUR**: Recteur de l'université
- **DOYEN**: Doyen de faculté
- **CHEF_DEPARTMENT**: Chef de département
- **ENSEIGNANT**: Enseignant
- **ETUDIANT**: Étudiant
- **PERSONNEL_ADMINISTRATIF**: Personnel administratif

### Permissions granulaires
- READ_USERS, CREATE_USER, UPDATE_USER, DELETE_USER
- READ_COURSES, CREATE_COURSE, UPDATE_COURSE, DELETE_COURSE
- READ_STUDENTS, CREATE_STUDENT, UPDATE_STUDENT, DELETE_STUDENT
- READ_TEACHERS, CREATE_TEACHER, UPDATE_TEACHER, DELETE_TEACHER
- READ_ENROLLMENTS, CREATE_ENROLLMENT, UPDATE_ENROLLMENT, DELETE_ENROLLMENT
- READ_GRADES, CREATE_GRADE, UPDATE_GRADE, DELETE_GRADE
- MANAGE_ROLES, MANAGE_PERMISSIONS, GENERATE_REPORTS, SYSTEM_ADMIN

---

## 🚀 Installation et Configuration

### Prérequis
- Java 17+
- MySQL 8.0+
- Maven 3.8+

### 1. Configuration Base de Données

```sql
CREATE DATABASE gestion_unchk CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configuration application.properties

Mettre à jour `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/gestion_unchk
spring.datasource.username=root
spring.datasource.password=votre_password

# JWT
app.jwt.secret=VOTRE_CLEF_SECRETE_LONGUE_ET_SECURISEE
app.jwt.expiration=86400000

# Server
server.port=8080
```

### 3. Démarrer l'application

```bash
# Compilation
mvn clean install

# Démarrage
mvn spring-boot:run

# L'API sera accessible sur: http://localhost:8080/api
```

---

## 📡 API Endpoints

### 🔓 Endpoints Publics (Authentication)

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}

Response 200:
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

#### Signup
```http
POST /api/v1/auth/signup
Content-Type: application/json

{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "SecurePassword123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "+221771234567"
}

Response 201: "Utilisateur enregistré avec succès"
```

### 👥 Endpoints Users (Authentification requise)

#### Lister tous les utilisateurs
```http
GET /api/v1/users
Authorization: Bearer <token>

Response 200:
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@unchk.edu.sn",
    "firstName": "Admin",
    "lastName": "System",
    ...
  }
]
```

#### Créer un utilisateur (ADMIN uniquement)
```http
POST /api/v1/users
Authorization: Bearer <token>
Content-Type: application/json

{
  "username": "etudiant",
  "email": "etudiant@unchk.edu.sn",
  "firstName": "Ahmed",
  "lastName": "Fall",
  "isActive": true
}

Response 201: Created
```

### 🎓 Endpoints Étudiants

#### Lister les étudiants
```http
GET /api/v1/students
Authorization: Bearer <token>
```

#### Créer un étudiant
```http
POST /api/v1/students
Authorization: Bearer <token>
Content-Type: application/json

{
  "studentNumber": "UNC2024001",
  "program": "Informatique",
  "enrollmentYear": 2024,
  "gender": "M",
  "address": "123 Rue..."
}
```

### 📚 Endpoints Cours

#### Lister les cours
```http
GET /api/v1/courses
Authorization: Bearer <token>
```

#### Créer un cours
```http
POST /api/v1/courses
Authorization: Bearer <token>
Content-Type: application/json

{
  "courseCode": "INF101",
  "title": "Introduction à l'Informatique",
  "credits": 3,
  "program": "Informatique",
  "semester": 1,
  "teacherId": 1
}
```

### ✍️ Endpoints Notes

#### Créer une note
```http
POST /api/v1/grades
Authorization: Bearer <token>
Content-Type: application/json

{
  "score": 85.5,
  "letterGrade": "A",
  "comments": "Excellent travail",
  "studentId": 1,
  "courseId": 1,
  "teacherId": 1
}
```

---

## 📖 Documentation Swagger

L'API est documentée via Swagger/OpenAPI et accessible à:

```
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

---

## 🔍 Exemple d'utilisation complet

### 1. Inscription

```bash
curl -X POST http://localhost:8080/api/v1/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "etudiant1",
    "email": "etudiant@example.com",
    "password": "SecurePass123",
    "firstName": "Ahmed",
    "lastName": "Sow"
  }'
```

### 2. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "etudiant@example.com",
    "password": "SecurePass123"
  }'

# Response:
# {
#   "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
#   "tokenType": "Bearer",
#   "expiresIn": 86400000
# }
```

### 3. Utiliser le token

```bash
TOKEN="eyJhbGciOiJIUzUxMiJ9..."

curl -X GET http://localhost:8080/api/v1/students \
  -H "Authorization: Bearer $TOKEN"
```

---

## 🛡️ Sécurité

### Best Practices Implémentées

✅ **JWT Stateless**: Pas de sessions serveur  
✅ **HS512 Signing**: Signature cryptographique forte  
✅ **Password Encryption**: BCrypt pour les mots de passe  
✅ **Role-Based Access**: Contrôle d'accès basé sur les rôles  
✅ **Permission-Based**: Permissions granulaires  
✅ **Input Validation**: Validation des données entrantes  
✅ **Exception Handling**: Gestion centralisée des erreurs  
✅ **HTTPS Ready**: Prêt pour HTTPS en production  

### Configuration de sécurité

```java
// SecurityConfig.java
- Stateless sessions (JWT)
- Authentification par Bearer token
- CORS configurable
- Rate limiting ready
- CSRF protection
- Exception handling personnalisé
```

---

## 📊 Structure des DTOs

### LoginRequest / LoginResponse
```java
// Request
email: String
password: String

// Response
accessToken: String
tokenType: String (Bearer)
expiresIn: Long
```

### UserDTO
```java
id: Long
username: String
email: String
firstName: String
lastName: String
phoneNumber: String
isActive: Boolean
roles: Set<RoleDTO>
```

### StudentDTO
```java
id: Long
studentNumber: String
program: String
enrollmentYear: Integer
gpa: Double
userId: Long
```

---

## 🐛 Gestion des erreurs

Tous les erreurs retournent un format standardisé:

```json
{
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Utilisateur non trouvé avec l'ID: 999",
  "timestamp": "2024-06-01T10:30:00",
  "path": "/api/v1/users/999"
}
```

### Codes d'erreur
- `200`: OK
- `201`: Created
- `204`: No Content
- `400`: Bad Request (Validation Error)
- `401`: Unauthorized (Token invalide/expiré)
- `403`: Forbidden (Accès refusé)
- `404`: Not Found (Ressource inexistante)
- `409`: Conflict (Ressource existe déjà)
- `500`: Internal Server Error

---

## 📝 Conventions de code

### Naming Conventions
- **Classes**: PascalCase (UserService, StudentDTO)
- **Methods**: camelCase (getUserById, createUser)
- **Constants**: UPPER_SNAKE_CASE (JWT_SECRET)
- **Endpoints**: kebab-case (/api/v1/students)

### Package Structure
```
com.tall.GestionUnchk/
├── config/          # Configurations (Security, Swagger)
├── controller/      # REST Controllers
├── service/         # Logique métier
├── repository/      # Accès données
├── entity/          # Modèles JPA
├── dto/             # Data Transfer Objects
├── security/        # JWT & Sécurité
├── exception/       # Gestion erreurs
├── mapper/          # Conversions Entity ↔ DTO
├── enums/           # Énumérations
└── util/            # Utilitaires
```

---

## 🧪 Tests et Validation

### Validation JPA
```java
@NotBlank(message = "Le champ est requis")
@Email(message = "Format email invalide")
@Size(min = 8, message = "Minimum 8 caractères")
```

### Validation Swagger
```yaml
CourseDTO:
  courseCode:
    type: string
    minLength: 3
    maxLength: 20
  title:
    type: string
    minLength: 1
    maxLength: 200
```

---

## 📦 Déploiement

### Build pour Production

```bash
# Build JAR
mvn clean package -DskipTests

# Fichier généré: target/GestionUnchk-1.0.0.jar
```

### Variables d'environnement

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/gestion_unchk
export SPRING_DATASOURCE_USERNAME=db_user
export SPRING_DATASOURCE_PASSWORD=secure_password
export APP_JWT_SECRET=production_secret_key_very_long_and_secure

java -jar GestionUnchk-1.0.0.jar
```

---

## 🤝 Contribution

Pour contribuer au projet:

1. Fork le repository
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

---

## 📞 Support

Pour toute question ou problème:
- Email: support@unchk.edu.sn
- Issues: GitHub Issues
- Documentation: `/swagger-ui.html`

---

## 📄 Licence

Ce projet est sous licence Apache 2.0 - voir [LICENSE](LICENSE)

---

## 👏 Auteurs

- **Architecte Backend**: Votre Nom
- **Date**: Juin 2024
- **Version**: 1.0.0

---

**Made with ❤️ for UNCHK University**
