# Atelier 4 — Inversion de contrôle, injection de dépendances et couche Service

## A. Modes d’injection comparés

| Critère | Injection par constructeur | Injection par attribut |
|---|---|---|
| Champ `final` possible | Oui | Non |
| Dépendance visible dans la signature du constructeur | Oui | Non |
| Utilisation hors conteneur Spring | Possible en fournissant la dépendance | Risque de dépendance `null` |
| Signalement par SonarQube | Pas de problème lié à l’injection par attribut | Règle S6813 à vérifier dans SonarQube |

## B. Services

| Service | Dépendances injectées | Mode d’injection | Justification |
|---|---|---|---|
| `AgenceServiceImpl` | `IAgenceRepository` | Constructeur | Réduit le couplage et facilite les tests. |
| `EmployeServiceImpl` | `IEmployeRepository` | Constructeur | Déclare explicitement les dépendances obligatoires. |
| `VehiculeServiceImpl` | `IVehiculeRepository` | Constructeur | Permet à Spring de fournir le repository. |
| `EquipementServiceImpl` | `IEquipementRepository` | Constructeur | Favorise une architecture faiblement couplée. |
| `ClientServiceImpl` | `IClientRepository` | Constructeur | Facilite la maintenance et les tests. |
| `ReservationServiceImpl` | `IReservationRepository` | Constructeur | Garantit l’injection des dépendances nécessaires. |
| `ContratServiceImpl` | `IContratRepository` | Constructeur | Respecte le modèle présenté dans l’atelier. |
| `PaiementServiceImpl` | `IPaiementRepository` | Constructeur | Permet la consultation des paiements en lecture seule. |
| `MaintenanceServiceImpl` | `IMaintenanceRepository` | Constructeur | Respecte le principe d’injection par constructeur. |

## C. Messages d’erreur du conteneur Spring

### Message 1 — NoSuchBeanDefinitionException

**Cause :** Spring ne trouve aucun bean de type `IContratService`. Cela peut être dû à l’absence de `@Service` sur `ContratServiceImpl` ou au fait que la classe se trouve en dehors du package scanné.

**Correction :** vérifier la présence de `@Service`, l’implémentation de `IContratService` et le package de la classe.

### Message 2 — NoUniqueBeanDefinitionException

**Cause :** Spring trouve deux beans compatibles (`emailNotificateur` et `smsNotificateur`) alors qu’un seul est attendu.

**Correction :** utiliser `@Primary` sur la classe du bean choisi par défaut, ou `@Qualifier` sur le paramètre du constructeur pour désigner explicitement le bean souhaité.

### Message 3 — BeanCurrentlyInCreationException

**Cause :** une dépendance circulaire existe entre `ClientServiceImpl` et `ReservationServiceImpl`.

**Correction :** revoir la conception pour supprimer la dépendance circulaire, par exemple en extrayant la responsabilité commune dans un troisième service.

### Gestion des exceptions

L’exception personnalisée `ResourceNotFoundException` est utilisée lorsqu’une ressource recherchée n’existe pas.

Elle permet de signaler clairement l’absence d’une entité à partir de son identifiant, plutôt que de laisser l’application poursuivre avec une valeur inexistante.

Les règles métier empêchent également certaines opérations invalides, par exemple la création d’une entité avec un identifiant déjà renseigné ou l’utilisation de valeurs numériques négatives lorsque celles-ci sont interdites.

## D. Anomalies SonarQube for IDE

### Résultats de l'analyse

L'analyse réalisée avec SonarQube for IDE le 26 octobre 2026 n'a détecté aucun problème dans le rapport affiché.

| Élément analysé | Résultat |
|---|---|
| Issues | Aucune |
| Security Hotspots | Aucun |
| Taint Vulnerabilities | Aucune |

### Corrections apportées

Aucune correction n'était nécessaire pour les éléments affichés dans ce rapport, car aucune anomalie n'a été détectée.

Le résultat concerne uniquement l'analyse présentée dans la capture d'écran. Il ne garantit pas l'absence de problèmes dans l'ensemble du projet.

## E. Questions de compréhension

### 1. Où est le `new` de `ContratServiceImpl` et qui l'exécute ?

Il n'y a pas de `new ContratServiceImpl` écrit manuellement dans le code. C'est le conteneur Spring IoC qui crée le service grâce à `@Service` et lui injecte le repository par constructeur.

### 2. Pourquoi un contrôleur dépendra-t-il de `IContratService` et non de `ContratServiceImpl` ?

Parce que l'interface réduit le couplage entre les couches et permet de changer l'implémentation sans modifier le contrôleur.

### 3. Pourquoi un service singleton doit-il rester sans état ? Donner un exemple de bug.

Un service singleton est partagé entre plusieurs requêtes. Un attribut modifiable contenant des données propres à une requête peut provoquer des conflits entre utilisateurs. Par exemple, deux requêtes pourraient modifier simultanément un contrat courant et utiliser les mauvaises données.

### 4. Pourquoi `update` charge-t-il l'entité existante avant d'appeler `save` au lieu de lui passer directement l'objet reçu ?

Pour conserver les données existantes et recopier uniquement les attributs modifiables. Cela évite d'écraser involontairement des colonnes ou de modifier la collection de paiements associée au contrat.

### 5. Quelle différence entre `@Component` et `@Bean` ? Entre `@Primary` et `@Qualifier` ? Pourquoi `IPaiementService` n'expose-t-il ni création, ni modification, ni suppression ?

- `@Component` : annotation placée sur une classe pour permettre sa détection automatique par Spring.
- `@Bean` : annotation placée sur une méthode de configuration pour déclarer explicitement un objet géré par Spring.
- `@Primary` : désigne le bean privilégié lorsqu'il existe plusieurs candidats.
- `@Qualifier` : permet de sélectionner explicitement un bean précis.
- `IPaiementService` est en lecture seule : la création, la modification et la suppression des paiements passent par le service Contrat.