# QuoiDNeuf
## WhatsApp-like - Projet d'Administration de Base de Données (Licence DA2I)
### Edouard Cattez - Thomas Ferro

---------------------

## Sommaire

- Introduction
 - En quelques mots...
 - Liste des fonctionnalités
 - Tutoriel de déploiement
     - Note sur les scripts SQL
     - Exemple avec PostgreSQL
     - SSL
     - Pool de connexion
     - Définition des mails
     - Accès à l'application
- Documentation utilisateur
 - Objectifs pour l'utilisateur
 - Guide d'utilisation
     - Login et inscription
     - Page de profil
     - Discussions
- Description technique
 - Objectifs remplis
 - Améliorations à apporter 
 - Principe de réalisation
 - Data Access Objects
    - MCD
    - MPD
 - Pseudo REST
     - /api/authentication
     - /api/discussions
     - /api/messages
     - /api/friends
     - /api/profiles
     - /api/recover
     - /api/mails
     - /api/files
     - JSON
     - Ticket
 - Difficultés techniques rencontréées et solutions apportées
 - Conclusion
- Conclusion générale

---------------------
## Introduction

### En quelques mots...

QuoiDNeuf est une application Web de messagerie instantannée semblable à [WhatsApp](http://www.whatsapp.com).

### Liste des fonctionnalités

QuoiDNeuf dispose d'une base solide de fonctionnalités :

- Messagerie en temps réel (2 à n utilisateurs)
- Interface web simple, responsive et user-friendly, validée par le W3C
- Profil personnalisable
- Recherche et ajout d'amis
- Envoi de photos

### Tutoriel de déploiement

Dans un premier temps, décompressez l'archive du projet (format `tar.gz`). Vous obtenez l'arborescence suivante :

| **Fichier** | **Description** |
| :------ | :---------- |
| compte-rendu/ | le compte-rendu au format HTML |
| sql/ | scripts à exécuter dans le SGBD de votre choix |
| lib/ | bibliothèques JAVA à placer dans le dossier **$TOMCAT/lib** |
| quoidneuf.war | application à placer dans le dossier **$TOMCAT/webapps** |

#### Note sur les scripts SQL

Le script **01_init.sql** préparera les différentes tables (attention, ce script écrasera vos tables si elles ont le même nom). Le second script **02_init.sql** est optionnel car il inscrit des données d'exemple dans la base afin de tester votre installation.

#### Exemple avec PostgreSQL

Ce tutoriel ne décrit pas l'installation de ce SGBD mais simplement la mise en place des tables nécessaires à l'application. Pour plus d'informations sur PostgreSQL, rendez-vous sur [le site officiel](http://www.postgresql.org/).

Exécutez avec la commande `psql -h <adresse> <base> -U <utilisateur> -f <fichier>` avec `<fichier>` le script à exécuter. 

#### SSL

Préparez votre serveur Tomcat pour l'utilisation du SSL, vous trouverez toutes les informations dans la [documentation officiel](https://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html).

- Décommentez le connecteur SSL dans le fichier **$TOMCAT/conf/server.xml**.
- Générez enfin la sécurité du serveur avec la commande **$JAVA_HOME/bin/keytool -genkey -alias tomcat -keyalg RSA**. Vous spécifierez comme mot de passe **changeit**.

#### Pool de connexion

Une fois votre base de données configurée et votre serveur Tomcat démarré, déplacez-vous dans le dossier **webapps/quoidneuf/** et ouvrez le fichier **META-INF/context.xml**. Editez vos informations de connexion pour la base de données. Un redémarrage du contexte sera peut-être nécessaire pour prendre en compte les modifications.

#### Définition des mails

Tout comme pour le pool de connexion, déplacez-vous dans le dossier **webapps/quoidneuf/** et ouvrez le fichier **META-INF/context.xml**. Éditez vos informations d'identification pour le serveur de mail que vous utilisez puis redémarrez le contexte de l'application.

#### Accès à l'application

Vous pouvez maintenant relancer votre serveur et accéder à votre application depuis l'adresse `https://<adresse_du_serveur>:8443/quoidneuf`.

Lors du premier accès, votre navigateur pourra vous mettre en garde d'un problème de certificat. Cette mise en garde se produit car nous n'utilisons pas de certificat validé par un tiers. Toutefois, vous pourrez accéder à l'application en indiquant à votre navigateur que celle-ci est digne de confiance.

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

![Screen ajout ami](https://raw.githubusercontent.com/ecattez/quoidneuf/master/screens/screen_nouvel_ami.png)

Présente aussi dans la barre de navigation, une liste déroulante de discussion avec possibilité d'en créer une nouvelle et un lien pour se déconnecter de l'application.

Voici enfin la présentation succincte des différentes pages de notre application.

#### Login et inscription

![Screen login](https://raw.githubusercontent.com/ecattez/quoidneuf/master/screens/screen_login.png)

La partie authentification est composée de deux champs, un pour le login et l'autre pour le mot de passe, et d'un lien pour récupérer votre mot de passe. 

L'application vérifie vos données d'authentification deux fois, une première directement chez le client, pour éviter d'engorger le réseau et de ralentir les utilisateurs disposants d'une connexion limitée, et une seconde sur le serveur pour éviter les attaques basiques et assurer un service sécurisé et fiable.

Vous pouvez, de plus, utiliser le service de récupération de mot de passe en cliquant sur le lien correspondant sous la zone de login. Une fenêtre modale s'ouvrira et vous invitera à entrer votre login et votre adresse e-mail afin de vous y envoyer un nouveau mot de passe créé aléatoirement. Vous pourrez modifier ce mot de passe aléatoire une fois sur votre page de profil (décrite plus loin).

![Screen récupération mot de passe](https://raw.githubusercontent.com/ecattez/quoidneuf/master/screens/screen_reset_password.png)

La partie inscription, quant à elle, contient les champs obligatoires (indiqués par un astérisque) et quelques champs optionnels. Une vérification de ces champs est effectuée avant l'envoi, pour les mêmes raisons que pour le login.

La réussite ou non des actions d'authentification, de récupération de mot de passe et de création de compte est directement visible sur la page à l'aide de messages chargés dynamiquement. 

Notez que cette application utilise une connexion sécurisée et que vos données seront donc à l'abris des sniffer de réseaux.

#### Page de profil

![Screen profil](https://raw.githubusercontent.com/ecattez/quoidneuf/master/screens/screen_profil.png)

La page de profil a des parties communes à tous les utilisateurs.
Le champs **Amis** contenant, comme son nom l'indique, les amis validés de l'utilisateur en question et ses informations (nom, prénom, date de naissance, photo de profil etc..).

Certains éléments de la page ne sont chargés que dans des cas précis. 
- Si la page de profil et celle de l'utilisateur connecté, ce dernier aura la possibilité de modifier les informations de son compte, changer sa photo de profil ou son mot de passe à l'aide de fenêtres modales).
- Si c'est la page d'un ami de l'utilisateur courant, ce dernier aura la possibilité de le retirer de sa liste.
- Enfin, si aucun lien ne lie les deux utilisateurs, une demande d'ami pourra être envoyée.

L'application charge toute la page une fois puis recharge les éléments dynamiquement afin d'offrir une navigation fluide et alléger la charge réseau.

#### Discussions

![Screen discussion](https://raw.githubusercontent.com/ecattez/quoidneuf/master/screens/screen_discussion.png)

La page de discussion est somme toute assez épurée, une zone d'affichage des messages sous forme de bulles de texte, une zone de saisie de message avec possibilité d'envoyer un document grâce à un formulaire affiché dans une fenêtre modale et la zone des membres.

Cette dernière n'est pas visible par défaut afin de ne pas polluer l'espace principal, surtout sur mobile. Vous pouvez la déplier en cliquant sur le bouton **Membres**. Cette zone présente tous les membres abonnés à la discussion et permet d'en ajouter à l'aide d'une zone de recherche alimentant un tableau, le tout affiché dans une fenêtre modale.

![Screen ajout membre](https://raw.githubusercontent.com/ecattez/quoidneuf/master/screens/screen_ajout_membre.png)

Comme pour toutes les pages, les erreurs ou réussites d'actions sont indiqués dans des zones prévues à cette effet. 

L'application ne charge qu'une seule fois la page si vous changez de discussion.

---------------------

## Description technique

### Objectifs remplis

| **Objectif** | **Réalisé Par** |
| :------: | :---------: |
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

| **Technologie** | **Raison de son utilisation** | **Dans quelle partie du projet** |
| :-: | :-: | :-: |
| J2EE | Servlets | Serveur |
| Jackson | Liaison objets JAVA/JSON | Serveur -> Client |
| CommonsIO/FileUtils | Upload de fichiers | Client -> Serveur |
| CommonsLang/StringEscapeUtils | Transformation html/texte | Client -> Serveur |
| Logger | Trace des événements | Serveur |
| Valve | Journal des logs + accès restreint à l'application | Serveur |
| JavaMail | Messagerie via JAVA | Serveur -> Client |
| HTML/CSS | Standards du Web | Vues de l'application |
| Bootstrap | Mise en page aisée pour desktop et mobile avec un design efficace | Vues de l'application |
| Javascript (JQuery) | Solution simple, documentée et éprouvée pour rendre dynamique son application Web | Liaison modèle, requêtes Ajax, pages dynamiques | 
| EL Expression | Simple d'utilisation, pratique pour récupérer des éléments de contextes différents | Liaison modèle, au chargement des pages de l'application | 
| Dynatable (Plugin JQuery) | Tableau dynamiques simple et efficace  à l'utilisation | Ajout de membres / d'amis |
| jQueryForm (Plugin JQuery) | Upload de fichier par requête Ajax simplifié | Changement photo de profil + upload image dans une discussion  |

### Data Access Objects

#### MCD

![Screen MCD](https://raw.githubusercontent.com/ecattez/quoidneuf/master/screens/quoidneuf_mcd.png)

#### MPD

![Screen MPD](https://raw.githubusercontent.com/ecattez/quoidneuf/master/screens/quoidneuf_mpd.png)

Afin de découper au mieux l'application, les appels à la base de données ne sont pas directement exécutés par les servlets. En effet, ces services délèguent le travail aux autorités compétentes : les DAOs. Ces objets sont classés par thèmes (authentification, discussions, amis...) et n'existent chacun qu'une et une seule fois dans toute l'application. Les services appellent les DAOs par l'intermédiaire du DaoProvider qui conserve les instances uniques de ces objets.

### Pseudo REST

Pour ce projet, nous avons décidé de faire du 'Pseudo' [REST](https://fr.wikipedia.org/wiki/Representational_State_Transfer) en utilisant les méthodes **doGet**, **doPost**, **doPut** et **doDelete** des Servlets. Les Servlets sont alors des services découpés par thèmes (authentification, discussions, amis...) dont les comportements sont décrits ci-dessous :

#### /api/authentication

| Méthode HTTP | Paramètres | Description |
| :----------- | :--------- | :---------- |
| **POST** | username, password | Connexion d'un utilisateur |
| **PUT** | password | Modifie le mot de passe d'un utilisateur |
| **DELETE** | - | Déconnexion d'un utilisateur |

#### /api/discussions

| Méthode HTTP | Paramètres | Description |
| :----------- | :--------- | :---------- |
| **GET** | id | Récupère une discussion |
| **POST** | - | Crée une discussion |
| **PUT** | id, user | Ajoute un utilisateur dans une discussion |
| **DELETE** | id | Retire un utilisateur d'une discussion |

#### /api/messages

| Méthode HTTP | Paramètres | Description |
| :----------- | :--------- | :---------- |
| **POST**| discussion, content | Ecrit un message dans une discussion |

#### /api/friends

| Méthode HTTP | Paramètres | Description |
| :----------- | :--------- | :---------- |
| **GET** | id, status | Récupère les (demandes d')amis d'un utilisateur |
| **POST** | friend | Crée une demande d'ami à un utilisateur |
| **PUT** | friend | Accepte la demande d'ami d'un utilisateur |
| **DELETE** | friend | Supprime l'amitié ou la demande d'amitié d'un utilisateur |

#### /api/profiles

| Méthode HTTP | Paramètres | Description |
| :----------- | :--------- | :---------- |
| **GET** | id | Récupère le profil d'un utilisateur |
| **POST** | username, password, firstname, lastname, birthday, description, email, phone | Crée un profil utilisateur |
| **PUT** | password, description, email, phone | Modifie le profil utilisateur |
| **DELETE** | - | Supprime le profil utilisateur |

#### /api/recover

| Méthode HTTP | Paramètres | Description |
| :----------- | :--------- | :---------- |
| **GET** | firstname, lastname, email | Récupère les utilisateurs répondant un des critères de recherche |
| **POST** | username, email | Crée une récupération de mot de passe avec appel au service de mail |

#### /api/mails

| Méthode HTTP | Paramètres | Description |
| :----------- | :--------- | :---------- |
| **POST** | email, title, content | Crée et envoie un email |

#### /api/files

| Méthode HTTP | Paramètres | Description |
| :----------- | :--------- | :---------- |
| **POST** | dest, folder, file | Télécharge un fichier sur le serveur (pour profil et discussion) |

*Note: contrairement au service /recover qui appelle /mails, le service /files demande directement si c'est un téléchargement au niveau du profil utilisateur ou bien au niveau de la discussion. Il faudrait diviser le système à terme.*

#### JSON

Tous les services renvoient des contenus au format JSON apportant un descriptif supplémentaire dans les réponses HTTP renvoyées aux clients. Cette façon de procéder permet de manipuler des objets coté client (JavaScript) et de privilégier l'Ajax pour la fluidité de l'application.

#### Ticket

Le Ticket est un objet particulièrement envoyé au client. Sa fonction est d'indiquer ce qui se produit en cas de succès ou d'échec des requêtes formulées par le client. C'est un objet très simple en soit mais d'une redoutable efficacité. Il est composé :

- d'un code de retour (code HTTP)
- d'un message adéquat à la situation

### Difficultés techniques rencontrées et solutions apportées

| **Problème** | **Temps de résolution (en heures)** | **Commentaire** |
| :----------- | :---------------------- | :-------------- |
| Upload AJAX | 4h | jQuery ne permet pas nativement d'envoyer correctement des formulaires à enctype multipart-formdata. Il a fallu mettre en place une extension spécifique pour résoudre ce problème (jQuery Form Plugin) |
| Upload Service | 4h | Problème algorithmique pour la copie d'images à la fois pour les photos de profils et les discussions |
| Responsive Design | 12h d'apprentissage + 12h de mise en place | Utilisation de Bootstrap coté front dans tout le projet |

### Conclusion

A première vue, ce projet peut paraître simple à réaliser. Toutefois, plusieurs problématiques apparaissent dès lors que l'on visualise le projet dans sa globalité. La mise en place de plusieurs technologies, aussi bien côté client que côté serveur, a permis de résoudre facilement ces problématiques et de rendre le développement facile et efficace. L'application est alors à l'effigie de son développement.

---------------------

## Conclusion générale

En définitive, ce projet nous a permis de mettre en place de nombreuses technologies Web. 

De plus, la conception d'une application de cette ampleur est essentielle pour intégrer toutes les notions vues en cours tout en découvrant de nouvelles techniques à la mode ou bien ancrées dans le temps. Ainsi, une grande partie des objectifs a été réalisée dans le respect du cahier des charges et des bonnes pratiques de développement.

Enfin, le travail d'équipe fut parfait car dès lors que le modèle et des normes furent mis en place, le front-end (vues côté client) et le back-end (servlets côté serveur) ont pu être développés indépendamment l'un de l'autre ! Nous n'avons jamais empiété ne serait-ce qu'une seule fois sur le travail de l'un et de l'autre.
