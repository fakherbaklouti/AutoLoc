# Atelier 3 — Repository Spring Data JPA

## 1. Choix des interfaces Repository

| Interface | Étend | Justification |
|---|---|---|
| IAgenceRepository | JpaRepository<Agence, Long> | Fournit les opérations CRUD ainsi que les fonctionnalités JPA et de pagination/tri. |
| IEmployeRepository | JpaRepository<Employe, Long> | Fournit les opérations CRUD ainsi que les fonctionnalités JPA et de pagination/tri. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | Fournit les opérations CRUD ainsi que les fonctionnalités JPA et de pagination/tri. |
| IEquipementRepository | JpaRepository<Equipement, Long> | Fournit les opérations CRUD ainsi que les fonctionnalités JPA et de pagination/tri. |
| IClientRepository | JpaRepository<Client, Long> | Fournit les opérations CRUD ainsi que les fonctionnalités JPA et de pagination/tri. |
| IReservationRepository | JpaRepository<Reservation, Long> | Fournit les opérations CRUD ainsi que les fonctionnalités JPA et de pagination/tri. |
| IContratRepository | JpaRepository<Contrat, Long> | Fournit les opérations CRUD ainsi que les fonctionnalités JPA et de pagination/tri. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Fournit les opérations CRUD ainsi que les fonctionnalités JPA et de pagination/tri. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | Fournit les opérations CRUD ainsi que les fonctionnalités JPA et de pagination/tri. |

## 2. Analyse SonarQube for IDE

### Anomalie 1

- **Fichier :** `application.properties`
- **Règle :** `java:S6437`
- **Problème :** un secret était présent directement dans le code source.
- **Correction :** remplacement du mot de passe en clair par la variable d'environnement `${DB_PASSWORD}`.

### Anomalie 2

- **Fichier :** `application.properties`
- **Règle :** `secrets:S6703`
- **Problème :** le mot de passe de la base de données était présent dans le code.
- **Correction :** utilisation de la variable d'environnement `${DB_PASSWORD}` et configuration de cette variable dans la configuration d'exécution IntelliJ.

Après correction, une nouvelle analyse SonarQube for IDE indique :

- No issues to display
- No Security Hotspots to display
- No Taint Vulnerabilities to display

## 3. Vérification

L'application démarre correctement et Spring Data JPA détecte :

`Found 9 JPA repository interfaces.`

Les 9 repositories sont basés sur `JpaRepository<Entité, Long>`.

## 4. Couche Service

### Interfaces Service (`tn.esprit.autoloc.service`)

| Interface | Opérations |
|---|---|
| IAgenceService | create, getById, getAll, update, delete |
| IEmployeService | create, getById, getAll, update, delete |
| IVehiculeService | create, getById, getAll, update, delete |
| IEquipementService | create, getById, getAll, update, delete |
| IClientService | create, getById, getAll, update, delete |
| IReservationService | create, getById, getAll, update, delete |
| IContratService | create, getById, getAll, update, delete |
| IPaiementService | create, getById, getAll, update, delete |
| IMaintenanceService | create, getById, getAll, update, delete |

### Classes ServiceImpl

Chaque interface est implémentée par une classe `@Service` avec injection du repository via `@RequiredArgsConstructor` :

- AgenceServiceImpl
- EmployeServiceImpl
- VehiculeServiceImpl
- EquipementServiceImpl
- ClientServiceImpl
- ReservationServiceImpl
- ContratServiceImpl
- PaiementServiceImpl
- MaintenanceServiceImpl

En cas d'entité introuvable par ID, une `ResourceNotFoundException` (HTTP 404) est levée.

## 5. CRUD REST — entités choisies

Entités retenues pour le CRUD complet (les plus simples, sans modifier les associations existantes) :

- **Equipement**
- **Client**

### Endpoints Equipement — `/api/equipements`

| Méthode | Endpoint | Code HTTP |
|---|---|---|
| POST | `/api/equipements` | 201 |
| GET | `/api/equipements/{id}` | 200 / 404 |
| GET | `/api/equipements` | 200 |
| PUT | `/api/equipements/{id}` | 200 / 404 |
| DELETE | `/api/equipements/{id}` | 204 / 404 |

### Endpoints Client — `/api/clients`

| Méthode | Endpoint | Code HTTP |
|---|---|---|
| POST | `/api/clients` | 201 |
| GET | `/api/clients/{id}` | 200 / 404 |
| GET | `/api/clients` | 200 |
| PUT | `/api/clients/{id}` | 200 / 404 |
| DELETE | `/api/clients/{id}` | 204 / 404 |

Les controllers exposent des DTOs (`web.dto`) avec validation Jakarta. Les repositories ne sont jamais injectés dans les controllers.

## 6. Vérifications effectuées

- Build Maven (`mvn clean compile`) : OK
- Démarrage Spring Boot : OK
- Connexion MySQL : OK
- `Found 9 JPA repository interfaces.`
- Tomcat sur le port 8080
- `Started AutolocApiApplication`
- CRUD REST Client et Equipement testés (codes HTTP 201 / 200 / 204 / 404 / 400)
- Aucun mot de passe en clair dans `application.properties` (`${DB_PASSWORD}`)
