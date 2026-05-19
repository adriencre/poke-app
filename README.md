# 📱 PokéDex - Application Android Native

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-UI-green.svg?logo=android)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-orange.svg)
![API](https://img.shields.io/badge/API-PokéAPI-red.svg)

Une application Android native moderne, élégante et complète, développée en Kotlin avec Jetpack Compose. Elle sert de Pokédex interactif en exploitant les données de la célèbre [PokéAPI](https://pokeapi.co/).

## ✨ Fonctionnalités Principales

*   **Pokédex Complet** : Parcourez l'ensemble des Pokémon avec des images de haute qualité et un design soigné.
*   **Détails Approfondis** : Consultez les statistiques détaillées (HP, Attaque, Défense, etc.), les types, et découvrez la **chaîne d'évolution interactive** de chaque Pokémon.
*   **Cris des Pokémon 🔊** : Écoutez le cri officiel de chaque créature grâce au lecteur audio intégré directement sur l'écran de détail !
*   **Gestion des Favoris ❤️** : Sauvegardez vos Pokémon préférés pour y accéder rapidement, même sans connexion internet, grâce à une base de données locale robuste.
*   **UI Réactive & Animations** : Profitez d'une interface fluide, moderne et réactive construite à 100% avec Jetpack Compose.

## 🛠️ Stack Technique & Architecture

Le projet a été pensé pour être prêt pour la production, en respectant les standards de l'industrie et les recommandations officielles d'Android :

*   **Langage** : Kotlin
*   **Interface Graphique** : Jetpack Compose (UI déclarative)
*   **Architecture** : Clean Architecture (Couches : *Data*, *Domain*, *Presentation*) + Design Pattern **MVVM** (Model-View-ViewModel)
*   **Injection de Dépendances** : Dagger Hilt
*   **Persistance Locale** : Room Database
*   **Réseau & API** : Retrofit2 & OkHttp (Appels à la PokéAPI)
*   **Asynchronisme & Flux de données** : Coroutines & Kotlin Flow
*   **Chargement d'Images** : Coil (Optimisé pour Compose)

## 📁 Structure du Projet

Le projet suit la **Clean Architecture** pour garantir la séparation des responsabilités, la testabilité et la maintenabilité :

*   `presentation/` : Contient l'UI (Composants Compose), les ViewModels et les états (State).
*   `domain/` : Contient la logique métier, les modèles de domaine, et les interfaces des repositories (Use Cases).
*   `data/` : Contient l'implémentation des repositories, les sources de données locales (Room) et distantes (Retrofit), ainsi que les DTOs.

## 🚀 Installation & Lancement rapide

1.  **Cloner le dépôt :**
    ```bash
    git clone <votre-lien-github>
    cd poke
    ```

2.  **Ouvrir avec Android Studio :**
    Ouvrez le dossier du projet avec la dernière version d'Android Studio (Koala ou supérieure recommandée pour Compose).

3.  **Compiler et Lancer :**
    *   Laissez Gradle synchroniser les dépendances.
    *   Branchez un appareil physique ou lancez un émulateur.
    *   Cliquez sur le bouton **Run** (▶️) ou utilisez le terminal : `./gradlew installDebug`.

## 📸 Aperçu

*(Astuce : Remplacez les liens ci-dessous par de vraies captures d'écran de votre application une fois publiées)*

| Liste des Pokémon | Détails & Évolutions | Favoris Sauvegardés |
| :---: | :---: | :---: |
| ![Liste](https://via.placeholder.com/250x500.png?text=Ecran+Liste) | ![Détail](https://via.placeholder.com/250x500.png?text=Ecran+Detail) | ![Favoris](https://via.placeholder.com/250x500.png?text=Ecran+Favoris) |

## 🤝 Contribuer

Les suggestions, rapports de bugs et pull requests sont toujours les bienvenus ! N'hésitez pas à ouvrir une *issue* si vous souhaitez discuter d'une nouvelle fonctionnalité.

---
*Développé avec passion pour l'écosystème Android & l'univers Pokémon.*
