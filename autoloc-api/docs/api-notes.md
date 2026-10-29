# Atelier 5 — Spring MVC, REST et SpringDoc

## A. Endpoints de l'API REST

Les contrôleurs REST reçoivent les requêtes HTTP et délèguent le traitement aux interfaces de service. Les codes indiqués ci-dessous correspondent aux réponses attendues selon le résultat de l'opération.

| Contrôleur | Verbe et route | Service appelé | Code de succès | Codes d'erreur possibles |
|---|---|---|---|---|
| `AgenceController` | `GET /api/agences` | `IAgenceService` | 200 OK | 500 Internal Server Error |
| `AgenceController` | `GET /api/agences/{id}` | `IAgenceService` | 200 OK | 404 Not Found, 500 Internal Server Error |
| `AgenceController` | `POST /api/agences` | `IAgenceService` | 201 Created | 400 Bad Request, 500 Internal Server Error |
| `AgenceController` | `PUT /api/agences/{id}` | `IAgenceService` | 200 OK | 400 Bad Request, 404 Not Found |
| `AgenceController` | `DELETE /api/agences/{id}` | `IAgenceService` | 204 No Content | 404 Not Found, 500 Internal Server Error |
| `ClientController` | `GET /api/clients` | `IClientService` | 200 OK | 500 Internal Server Error |
| `ClientController` | `GET /api/clients/{id}` | `IClientService` | 200 OK | 404 Not Found |
| `ClientController` | `POST /api/clients` | `IClientService` | 201 Created | 400 Bad Request |
| `ClientController` | `PUT /api/clients/{id}` | `IClientService` | 200 OK | 400 Bad Request, 404 Not Found |
| `ClientController` | `DELETE /api/clients/{id}` | `IClientService` | 204 No Content | 404 Not Found |
| `VehiculeController` | `GET /api/vehicules` | `IVehiculeService` | 200 OK | 500 Internal Server Error |
| `VehiculeController` | `GET /api/vehicules/{id}` | `IVehiculeService` | 200 OK | 404 Not Found |
| `VehiculeController` | `POST /api/vehicules` | `IVehiculeService` | 201 Created | 400 Bad Request |
| `VehiculeController` | `PUT /api/vehicules/{id}` | `IVehiculeService` | 200 OK | 400 Bad Request, 404 Not Found |
| `VehiculeController` | `DELETE /api/vehicules/{id}` | `IVehiculeService` | 204 No Content | 404 Not Found |
| `ReservationController` | `GET /api/reservations` | `IReservationService` | 200 OK | 500 Internal Server Error |
| `ReservationController` | `GET /api/reservations/{id}` | `IReservationService` | 200 OK | 404 Not Found |
| `ReservationController` | `POST /api/reservations` | `IReservationService` | 201 Created | 400 Bad Request |
| `ReservationController` | `PUT /api/reservations/{id}` | `IReservationService` | 200 OK | 400 Bad Request, 404 Not Found |
| `ReservationController` | `DELETE /api/reservations/{id}` | `IReservationService` | 204 No Content | 404 Not Found |
| `ContratController` | `GET /api/contrats` | `IContratService` | 200 OK | 500 Internal Server Error |
| `ContratController` | `GET /api/contrats/{id}` | `IContratService` | 200 OK | 404 Not Found |
| `ContratController` | `POST /api/contrats` | `IContratService` | 201 Created | 400 Bad Request, 404 Not Found |
| `ContratController` | `PUT /api/contrats/{id}` | `IContratService` | 200 OK | 400 Bad Request, 404 Not Found |
| `ContratController` | `DELETE /api/contrats/{id}` | `IContratService` | 204 No Content | 404 Not Found |
| `PaiementController` | `GET /api/paiements` | `IPaiementService` | 200 OK | 500 Internal Server Error |
| `PaiementController` | `GET /api/paiements/{id}` | `IPaiementService` | 200 OK | 404 Not Found |
| `EmployeController` | `GET /api/employes` | `IEmployeService` | 200 OK | 500 Internal Server Error |
| `EmployeController` | `GET /api/employes/{id}` | `IEmployeService` | 200 OK | 404 Not Found |
| `EmployeController` | `POST /api/employes` | `IEmployeService` | 201 Created | 400 Bad Request |
| `EmployeController` | `PUT /api/employes/{id}` | `IEmployeService` | 200 OK | 400 Bad Request, 404 Not Found |
| `EmployeController` | `DELETE /api/employes/{id}` | `IEmployeService` | 204 No Content | 404 Not Found |
| `MaintenanceController` | `GET /api/maintenances` | `IMaintenanceService` | 200 OK | 500 Internal Server Error |
| `MaintenanceController` | `GET /api/maintenances/{id}` | `IMaintenanceService` | 200 OK | 404 Not Found |
| `MaintenanceController` | `POST /api/maintenances` | `IMaintenanceService` | 201 Created | 400 Bad Request |
| `MaintenanceController` | `PUT /api/maintenances/{id}` | `IMaintenanceService` | 200 OK | 400 Bad Request, 404 Not Found |
| `MaintenanceController` | `DELETE /api/maintenances/{id}` | `IMaintenanceService` | 204 No Content | 404 Not Found |
| `EquipementController` | `GET /api/equipements` | `IEquipementService` | 200 OK | 500 Internal Server Error |
| `EquipementController` | `GET /api/equipements/{id}` | `IEquipementService` | 200 OK | 404 Not Found |
| `EquipementController` | `POST /api/equipements` | `IEquipementService` | 201 Created | 400 Bad Request |
| `EquipementController` | `PUT /api/equipements/{id}` | `IEquipementService` | 200 OK | 400 Bad Request, 404 Not Found |
| `EquipementController` | `DELETE /api/equipements/{id}` | `IEquipementService` | 204 No Content | 404 Not Found |

**Remarque :** ce tableau présente les routes CRUD attendues. Il faut vérifier les routes exactes, les méthodes réellement présentes et les codes de réponse déclarés dans chaque contrôleur avant de considérer le tableau comme définitif.

## B. Annotations Jackson

Les entités JPA comportent des relations bidirectionnelles. Lorsqu'elles sont converties en JSON, ces relations peuvent provoquer des références circulaires ou exposer des informations qui ne sont pas nécessaires à la réponse.

| Association | Annotation retenue | Justification |
|---|---|---|
| `Contrat.paiements` | `@JsonManagedReference("contrat-paiements")` | Permet de sérialiser les paiements à partir du contrat sans parcourir indéfiniment la relation inverse. |
| `Paiement.contrat` | `@JsonBackReference("contrat-paiements")` | Empêche la sérialisation du retour vers le contrat et complète la référence gérée du côté `Contrat`. |
| `Reservation.contrat` | `@JsonIgnore` | Évite que la réservation renvoie vers son contrat, ce qui provoquerait une boucle dans le JSON. |
| `Agence.vehicules` | `@JsonIgnore` | Empêche la sérialisation de la collection des véhicules dans la réponse de l'agence afin d'éviter les références circulaires avec les entités associées. |

### Différence entre `@JsonBackReference` et `@JsonIgnore`

- `@JsonBackReference` sert à gérer une relation bidirectionnelle avec une annotation `@JsonManagedReference` correspondante. La propriété inverse n'est pas sérialisée dans cette direction.
- `@JsonIgnore` exclut directement la propriété concernée de la sérialisation JSON et, selon le contexte, de la désérialisation.

Dans notre projet, `Contrat.paiements` utilise `@JsonManagedReference` pour conserver les paiements dans la réponse du contrat. `Paiement.contrat` utilise `@JsonBackReference` pour éviter le retour circulaire. À l'inverse, `Agence.vehicules` utilise `@JsonIgnore` parce que l'objectif est de ne pas inclure cette collection dans la représentation JSON de l'agence.

## C. Postman et Swagger

### Postman

Postman permet d'envoyer des requêtes HTTP à l'API et de vérifier les réponses du serveur.

Il est utile pour :
- tester les méthodes GET, POST, PUT et DELETE ;
- envoyer des corps JSON ;
- vérifier les codes HTTP ;
- tester les cas nominaux et les erreurs ;
- reproduire un scénario composé de plusieurs requêtes.

**Exemple de l'atelier :** la création d'une réservation avec `POST /api/reservations` a renvoyé `201 Created` et l'identifiant `3`. Ensuite, un contrat a été créé avec `POST /api/contrats`, en utilisant la réservation `3` et deux paiements de `240,00`. La réponse a renvoyé `201 Created`. Enfin, `GET /api/paiements` a confirmé la présence des paiements créés.

### Swagger / SpringDoc

SpringDoc génère une documentation OpenAPI à partir des contrôleurs de l'application. Swagger UI présente cette documentation sous une interface graphique interactive.

Il permet notamment de :
- consulter les routes disponibles ;
- visualiser les méthodes HTTP et les paramètres ;
- examiner les schémas de requête et de réponse ;
- exécuter des requêtes directement depuis le navigateur.

**Exemple de l'atelier :** Swagger UI a permis de consulter et de tester les endpoints de l'API AutoLoc. Il a également facilité la préparation des corps JSON pour la création des réservations et des contrats.

### Comparaison

Postman est particulièrement adapté à la préparation de collections de tests, à la vérification de scénarios et à la conservation des requêtes. Swagger UI est principalement destiné à explorer la documentation OpenAPI et à tester rapidement les opérations exposées.

Les deux outils sont complémentaires : Swagger facilite la découverte de l'API, tandis que Postman permet d'organiser les tests et de les rejouer.

## D. Analyse statique avec SonarQube

L'analyse du projet avec SonarQube for IDE a détecté **19 problèmes répartis dans 7 fichiers**.

### Anomalie 1 : Utilisation directe des entités persistantes

- **Règle SonarQube :** `java:S4684`
- **Fichiers concernés :** plusieurs contrôleurs REST, notamment `AgenceController`, `ContratController`, `EmployeController`, `MaintenanceController`, `ReservationController` et `VehiculeController`.
- **Problème :** l'utilisation directe des entités JPA dans les contrôleurs crée un couplage entre l'API REST et le modèle de persistance.
- **Correction proposée :** utiliser des DTO pour séparer les données exposées par l'API des entités utilisées par la couche de persistance.

### Anomalie 2 : Duplication de chaînes de caractères

- **Règle SonarQube :** `java:S1192`
- **Fichier concerné :** `GlobalExceptionHandler.java`.
- **Problème :** plusieurs chaînes de caractères, notamment `"timestamp"`, `"status"`, `"error"` et `"message"`, sont répétées dans le code.
- **Correction proposée :** définir des constantes pour les chaînes réutilisées afin d'améliorer la lisibilité et de faciliter la maintenance.

### Autres remarques

SonarQube signale également la nécessité de préciser explicitement le fuseau horaire lors de certains appels à `now()`, conformément à la règle `java:S8688`.

Ces résultats constituent des recommandations d'amélioration du code. Ils doivent être examinés afin de déterminer les corrections pertinentes sans modifier inutilement les parties déjà validées du projet.
## E. Questions de compréhension

### 1. Quel composant reçoit toutes les requêtes, et comment retrouve-t-il la méthode du contrôleur à appeler ?

Dans Spring MVC, le `DispatcherServlet` reçoit les requêtes HTTP. Il s'appuie sur les mécanismes de mapping de Spring MVC, notamment `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping` et `@DeleteMapping`, pour identifier la méthode du contrôleur correspondant à la route et au verbe HTTP.

### 2. Pourquoi `ContratController` dépend-il de `IContratService` et non de `ContratRepository` ni de `ContratServiceImpl` ?

`ContratController` dépend de `IContratService` pour respecter la séparation des couches. Le contrôleur délègue la logique métier au service, qui utilise le repository pour accéder aux données. Dépendre de l'interface plutôt que de `ContratServiceImpl` réduit le couplage et facilite les tests ainsi que le remplacement de l'implémentation.

### 3. Pourquoi un POST renvoie-t-il 201 et un DELETE 204 ? Pourquoi une ressource absente donne-t-elle 404 et non 500 ?

- `201 Created` indique qu'une nouvelle ressource a été créée avec succès.
- `204 No Content` indique que l'opération a réussi et qu'aucun contenu n'est renvoyé, notamment après une suppression.
- `404 Not Found` indique que la ressource demandée n'existe pas.
- `500 Internal Server Error` correspond à une erreur interne inattendue du serveur. Une ressource absente est un cas identifiable et doit donc être traitée explicitement, par exemple avec `ResourceNotFoundException`.

### 4. Pourquoi `PaiementController` n'expose-t-il que des GET ? Quelle réponse reçoit un POST `/api/paiements` ?

Dans notre application, les paiements sont gérés à travers le contrat auquel ils sont associés. `PaiementController` est donc en lecture seule et expose les opérations de consultation.

Aucun endpoint POST n'est déclaré pour `/api/paiements`. Une requête `POST /api/paiements` ne correspond donc à aucune route déclarée et Spring MVC renvoie normalement `405 Method Not Allowed` lorsque le chemin existe mais que la méthode HTTP n'est pas autorisée.

### 5. Quels risques présente l'exposition directe des entités JPA ? Pourquoi `Contrat.paiements` utilise-t-il `@JsonManagedReference` alors que `Agence.vehicules` utilise `@JsonIgnore` ?

L'exposition directe des entités JPA peut entraîner :
- des références circulaires pendant la sérialisation JSON ;
- la divulgation de données internes ou de relations non nécessaires ;
- des réponses trop volumineuses ;
- un couplage entre le format de l'API et le modèle de persistance.

`Contrat.paiements` utilise `@JsonManagedReference` afin d'inclure les paiements dans la réponse du contrat tout en contrôlant la relation bidirectionnelle. `Agence.vehicules` utilise `@JsonIgnore` pour exclure directement cette collection du JSON de l'agence et éviter la sérialisation récursive.

Ces annotations répondent donc à deux besoins différents : conserver une relation utile dans la réponse d'un contrat et masquer une collection dans la réponse d'une agence.