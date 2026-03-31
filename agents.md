# Contexte du Projet : Refonte "Loto Associatif"

## Rôle et Objectif
L'objectif est de moderniser et de restructurer un projet open source existant (licence MIT) de gestion de loto associatif. Le projet initial souffre d'une dette technique importante (historiquement sous des versions obsolètes de Java, Travis CI, Launch4j). 

Cette refonte est développée et maintenue par SIM32 (Services Informatiques Miélan 32). Le logiciel cible doit devenir une solution robuste, fiable et facile à déployer pour les associations locales, tout en servant de vitrine de savoir-faire technique de l'entreprise. L'approche de développement doit être méthodique, sécurisée et axée sur l'assurance qualité (rigueur QSE : traçabilité, tests, code propre).

## Règles de Communication et de Code
* **Langue de l'interface et du dialogue :** L'agent IA doit impérativement interagir avec l'utilisateur (vous) en **français** dans la boîte de dialogue.
* **Langue du code :** Tous les commentaires, la documentation technique (Javadoc, README), et le nommage des variables/méthodes (si la convention du projet l'exige) doivent être rédigés en **français**.

## Outils et Stack Technique Cible
L'agent IA doit toujours cibler les dernières versions stables et LTS (Long Term Support) au moment de son intervention :
* **Langage :** Dernière version de Java LTS disponible et stable.
* **Interface Graphique :** OpenJFX (dernière version stable compatible avec le JDK choisi).
* **Build Tool :** Gradle (dernière version stable).
* **CI/CD :** GitHub Actions (en remplacement de Travis CI).
* **Qualité de code :** Analyse statique stricte (SonarLint), typage fort, séparation claire du modèle MVC.
* **Déploiement :** `jpackage` (inclus dans le JDK cible) pour la génération d'exécutables natifs sans dépendance Java requise sur le poste client.

## Nouvelles Fonctionnalités Requises
* **Historique étendu :** Affichage dynamique et ordonné des 15 derniers numéros sortis (au lieu du seul dernier numéro).
* **Compteur de progression :** Affichage en temps réel du nombre total de numéros déjà tirés.
* **Mode "Double Écran" (Affichage déporté) :** Création d'une fenêtre secondaire épurée, dédiée au public (grande grille, historique, compteur). Cette fenêtre est conçue pour s'afficher sur un écran étendu, permettant à l'animateur de garder l'interface de contrôle sur son écran principal et d'utiliser l'ordinateur pour d'autres tâches en parallèle (ex: diffusion de musique).

## Plan d'Action (Roadmap)

### Phase 1 : Assainissement et Modernisation
1. Mise à niveau du JDK vers la dernière version Java LTS et intégration d'OpenJFX comme dépendance explicite.
2. Mise à jour de la configuration `build.gradle` et du Wrapper Gradle vers les dernières versions stables.
3. Remplacement de `.travis.yml` par des workflows GitHub Actions pour la compilation automatisée.
4. Audit statique général, correction des avertissements et sécurisation des processus existants.
5. Fiabilisation du système de sauvegarde/restauration local des grilles (`GridStorage`).

### Phase 2 : Évolution du Backend (Logique Métier)
6. Modification des structures de données internes (`LotoGrid`) pour mémoriser l'ordre chronologique exact des tirages.
7. Implémentation des méthodes d'extraction pour récupérer spécifiquement la liste des 15 derniers numéros.
8. Implémentation de la méthode de calcul du compteur global de numéros tirés.

### Phase 3 : Refonte Frontend - Écran Animateur (Contrôle)
9. Intégration du composant visuel pour afficher le compteur global sur le tableau de bord.
10. Création et intégration du composant d'historique (file visuelle des 15 derniers numéros).
11. Optimisation de l'ergonomie de l'écran principal (agrandissement des boutons, ajout de raccourcis clavier) pour un confort d'animation maximal.

### Phase 4 : Implémentation de l'Affichage Déporté
12. Conception de la vue `PublicStage` (interface en lecture seule, sans boutons de contrôle).
13. Mise en place d'un *Data Binding* bidirectionnel robuste : une action sur l'écran animateur doit rafraîchir instantanément le modèle, l'écran animateur et l'écran public.
14. Détection automatisée des moniteurs multiples au lancement et positionnement intelligent de la fenêtre publique en plein écran sur l'écran secondaire.

### Phase 5 : Déploiement et Distribution
15. Suppression des anciens scripts d'exécutables et configuration de `jpackage` pour générer des installeurs autonomes, prêts à être distribués aux clients de l'entreprise.
