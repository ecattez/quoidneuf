# QuoiDNeuf
## WhatsApp-like - Projet d'Administration de Base de Données (Licence DA2I)
### Edouard Cattez - Thomas Ferro

---------------------

## Sommaire

- Introduction
 - En quelques mots...
 - Liste des fonctionnalités
 - Tutoriel de déploiement
- Documentation utilisateur
 - Objectifs pour l'utilisateur
 - Guide d'utilisation
- Description technique
 - Objectifs remplis et améliorations à apporter
 - Principe de réalisation
 - Difficultés techniques rencontréées et solutions apportées
 - Conclusion
- Conclusion générale

---------------------
## Introduction

### En quelques mots...

QuoiDNeuf est une application Web de messagerie instantannée semblable à [WhatsApp](www.whatsapp.com).

### Liste des fonctionnalités

QuoiDNeuf dispose d'une base solide de fonctionnalités :

- Messagerie en temps réel à deux comme en groupe
- Interface web simple, responsive et user-friendly, validée par le W3C
- Profil personnalisable
- Envoi de photos

Et de nombreuses autres, secondaires, à découvrir !

### Tutoriel de déploiement

TODO : Décrire ce qu'il faut télécharger, ce qu'il faut l'extraire et où et enfin ce qu'il faut configurer, avec des screens.

---------------------

## Documentation utilisateur

### Objectifs pour l'utilisateur

Nous avons développé cette application en suivant trois grandes lignes directrices :
- Offrir à l’utilisateur final un service de qualité, simple et efficace ;
- Proposer une application Web validée par le W3C et suivant la mode actuelle, avec une utilisation nomade aisée ;
- Obtenir une application basée sur un modèle robuste et réutilisable facilement pour des refontes voir des portages futurs.

Vous découvrirez plus loin dans cette documentation comment ce dernier point se traduit dans le code, nous allons cependant commencer par un développement des deux premiers éléments.

### Guide d'utilisation

Ce guide d’utilisation se découpera en trois parties, une pour chaque page principale de l’application.

Mais avant cela, voici une petite présentation de la barre de navigation, présente sur toutes les pages à l'exception de la page d'authentification.

Cette dernière comporte un lien pour aller sur son profil, un autre pour dérouler la liste de ses amis, des demandes d'amis en attente et une recherche d'utilisateur à ajouter (dans une fenêtre modale, avec un tableau dynamique).

Présente aussi dans la barre de navigation, une liste déroulante de discussion avec possibilité d'en créer une nouvelle et un lien pour se déconnecter de l'application.

Voici enfin la présentation succincte des différentes pages de notre application.

#### Login et inscription

**Screens**

La partie authentification est composée de deux champs, un pour le login et l'autre pour le mot de passe, et d'un lien pour récupérer votre mot de passe. 

L'application vérifie vos données d'authentification deux fois, une première directement chez le client, pour éviter d'engorger le réseau et de ralentir les utilisateurs disposants d'une connexion limitée, et une seconde sur le serveur pour éviter les attaques basiques et assurer un service sécurisé et fiable.

Vous pouvez, de plus, utiliser le service de récupération de mot de passe en cliquant sur le lien correspondant sous la zone de login. Une fenêtre modale s'ouvrira et vous invitera à entrer votre login et votre adresse e-mail afin de vous y envoyer un nouveau mot de passe créé aléatoirement. Vous pourrez modifier ce mot de passe aléatoire une fois sur votre page de profil (décrite plus loin).

La partie inscription, quant à elle, contient les champs obligatoires (indiqués par un astérisque) et quelques champs optionnels. Une vérification de ces champs est effectuée avant l'envoi, pour les mêmes raisons que pour le login.

La réussite ou non des actions d'authentification, de récupération de mot de passe et de création de compte est directement visible sur la page à l'aide de messages chargés dynamiquement. 

Notez que cette application utilise une connexion sécurisée et que vos données seront donc à l'abris des sniffer de réseaux.

#### Page de profil

**Screen**

La page de profil a des parties communes à tous les utilisateurs.
Le champs **Amis** contenant, comme son nom l'indique, les amis validés de l'utilisateur en question et ses informations (nom, prénom, date de naissance, photo de profil etc..).

Certains éléments de la page ne sont chargés que dans des cas précis. 
- Si la page de profil et celle de l'utilisateur connecté, ce dernier aura la possibilité de modifier les informations de son compte, changer sa photo de profil ou son mot de passe à l'aide de fenêtres modales).
- Si c'est la page d'un ami de l'utilisateur courant, ce dernier aura la possibilité de le retirer de sa liste.
- Enfin, si aucun lien ne lie les deux utilisateurs, une demande d'ami pourra être envoyée.

L'application charge toute la page une fois puis recharge les éléments dynamiquement afin d'offrir une navigation fluide et alléger la charge réseau.

#### Discussions

**Screen**

La page de discussion est somme toute assez épurée, une zone d'affichage des messages sous forme de bulles de texte, une zone de saisie de message avec possibilité d'envoyer un document grâce à un formulaire affiché dans une fenêtre modale et la zone des membres.

Cette dernière n'est pas visible par défaut afin de ne pas polluer l'espace principal, surtout sur mobile. Vous pouvez la déplier en cliquant sur le bouton **Membres**. Cette zone présente tous les membres abonnés à la discussion et permet d'en ajouter à l'aide d'une zone de recherche alimentant un tableau, le tout affiché dans une fenêtre modale.

Comme pour toutes les pages, les erreurs ou réussites d'actions sont indiqués dans des zones prévues à cette effet. 

L'application ne charge qu'une seule fois la page si vous changez de discussion.

---------------------

## Description technique

### Objectifs remplis

| Objectif | Réalisé Par |
| :-: | :---------: | :-----: |
| Concevoir un MCD conforme aux besoin de l'application | Edouard et Thomas | 
| Réalisation des scripts SQL pour la création des tables et des vues  |  Edouard et Thomas |
| Réalisation du modèle en Java (décrit en détail dans la partie correspondante) | Edouard |
| Réalisation des vues en HTML/CSS (avec Bootstrap) | Thomas | 
| Ajout des fonctionnalités avancées dans le modèle (SSL, Pool de connexion, etc.. là aussi décrit en détail plus loin) | Edouard |
| Liaison du modèle aux vues en Javascript (Ajax) | Thomas |

### Améliorations à apporter

Voici quelques exemples d'éléments à ajouter à notre projet dans le futur :
- Test de monté en charge avec **Apache JMeter**.
- Hébergement sur un serveur dédié et accessible en ligne.
- Travailler sur l'accessibilité de l'application pour les personnes handicapés.
- Modifier des éléments des vues HTML pour respecter les critères de qualité Web d'**Opquast**.
- Proposer un script d'installation et de déploiement rapide de l'application directement sur un serveur **Tomcat**.

### Principe de réalisation

| Technologie | Raison de son utilisation | Dans quelle partie du projet |
| :-: | :-: | :-: |
| Java | / | Servlets |
| De la place pour Edouard | / | / |
| HTML/CSS | Standards du Web | Vues de l'application |
| Bootstrap | Mise en page aisée pour desktop et mobile avec un design efficace | Vues de l'application |
| Javascript (JQuery) | Solution simple, documentée et éprouvée pour rendre dynamique son application Web | Liaison modèle, requêtes Ajax, pages dynamiques | 
| EL Expression | Simple d'utilisation, pratique pour récupérer des éléments de contextes différents | Liaison modèle, au chargement des pages de l'application | 
| Dynatable (Plugin JQuery) | Tableau dynamiques simple et efficace  à l'utilisation | Ajout de membres / d'amis |

### Difficultés techniques rencontrées et solutions apportées

TODO : cf le titre :) Peut-être sous forme d'un tableau "Problème/ Temps pour le régler / Commentaire"

### Conclusion

On peut donc conclure que l'utilisation de plusieurs technologies Web a permis de faciliter le développement tout en rendant l'application complète et efficace. 

---------------------

## Conclusion générale

TODO : Comparer les objectifs avec le résultat final + conclusion habituelle
