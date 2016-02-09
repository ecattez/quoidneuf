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

Pour déployer cette aplication sur votre serveur Tomcat, vérifiez que ce dernier n'est pas déjà lancé puis téléchargez l'achive `/tar/gz`, copiez le `.war` dans le dossier *webapps* à la racine de votre serveur. Copiez enfin les librairies que vous n'avez pas déjà dans votre dossier *lib*.

Il vous faudra ensuite préparer votre base de données PostgreSQL. Ce tutoriel ne décrit pas l'installation de ce service mais simplement la mise en place des tables nécessaires à l'application. Pour plus d'informations sur PostgreSQL, rendez-vous sur [le site officiel](http://www.postgresql.org/). 

Dans le dossier *SQL* de l'archive, vous trouverez deux script SQL. 

Le premier vous préparera les différentes tables. Notez que ce script écrasera vos tables si elles ont le même nom.

Le second script est optionnel, il inscrit des données d'exemple dans la base afin de tester votre installation.

Il vous faudra ensuite préparer votre serveur Tomcat pour l'utilisation du SSL, vous trouverez toutes les informations dans la [documentation officiel](https://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html).

Vous pouvez maintenant relancer votre serveur et accéder à votre application depuis l'adresse `https://<adresse_du_serveur>:8443/quoidneuf`.

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

![Screen ajout ami](/screens/screen_nouvel_ami.png)

Présente aussi dans la barre de navigation, une liste déroulante de discussion avec possibilité d'en créer une nouvelle et un lien pour se déconnecter de l'application.

Voici enfin la présentation succincte des différentes pages de notre application.

#### Login et inscription

![Screen login](/screens/screen_login.png)

La partie authentification est composée de deux champs, un pour le login et l'autre pour le mot de passe, et d'un lien pour récupérer votre mot de passe. 

L'application vérifie vos données d'authentification deux fois, une première directement chez le client, pour éviter d'engorger le réseau et de ralentir les utilisateurs disposants d'une connexion limitée, et une seconde sur le serveur pour éviter les attaques basiques et assurer un service sécurisé et fiable.

Vous pouvez, de plus, utiliser le service de récupération de mot de passe en cliquant sur le lien correspondant sous la zone de login. Une fenêtre modale s'ouvrira et vous invitera à entrer votre login et votre adresse e-mail afin de vous y envoyer un nouveau mot de passe créé aléatoirement. Vous pourrez modifier ce mot de passe aléatoire une fois sur votre page de profil (décrite plus loin).

![Screen récupération mot de passe](/screens/screen_reset_password.png)

La partie inscription, quant à elle, contient les champs obligatoires (indiqués par un astérisque) et quelques champs optionnels. Une vérification de ces champs est effectuée avant l'envoi, pour les mêmes raisons que pour le login.

La réussite ou non des actions d'authentification, de récupération de mot de passe et de création de compte est directement visible sur la page à l'aide de messages chargés dynamiquement. 

Notez que cette application utilise une connexion sécurisée et que vos données seront donc à l'abris des sniffer de réseaux.

#### Page de profil

![Screen profil](/screens/screen_profil.png)

La page de profil a des parties communes à tous les utilisateurs.
Le champs **Amis** contenant, comme son nom l'indique, les amis validés de l'utilisateur en question et ses informations (nom, prénom, date de naissance, photo de profil etc..).

Certains éléments de la page ne sont chargés que dans des cas précis. 
- Si la page de profil et celle de l'utilisateur connecté, ce dernier aura la possibilité de modifier les informations de son compte, changer sa photo de profil ou son mot de passe à l'aide de fenêtres modales).
- Si c'est la page d'un ami de l'utilisateur courant, ce dernier aura la possibilité de le retirer de sa liste.
- Enfin, si aucun lien ne lie les deux utilisateurs, une demande d'ami pourra être envoyée.

L'application charge toute la page une fois puis recharge les éléments dynamiquement afin d'offrir une navigation fluide et alléger la charge réseau.

#### Discussions

![Screen discussion](/screens/screen_discussion.png)

La page de discussion est somme toute assez épurée, une zone d'affichage des messages sous forme de bulles de texte, une zone de saisie de message avec possibilité d'envoyer un document grâce à un formulaire affiché dans une fenêtre modale et la zone des membres.

Cette dernière n'est pas visible par défaut afin de ne pas polluer l'espace principal, surtout sur mobile. Vous pouvez la déplier en cliquant sur le bouton **Membres**. Cette zone présente tous les membres abonnés à la discussion et permet d'en ajouter à l'aide d'une zone de recherche alimentant un tableau, le tout affiché dans une fenêtre modale.

![Screen ajout membre](/screens/screen_ajout_membre.png)

Comme pour toutes les pages, les erreurs ou réussites d'actions sont indiqués dans des zones prévues à cette effet. 

L'application ne charge qu'une seule fois la page si vous changez de discussion.

---------------------

## Description technique

### Objectifs remplis

| Objectif | Réalisé Par |
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

| Technologie | Raison de son utilisation | Dans quelle partie du projet |
| :-: | :-: | :-: |
| J2EE | Servlets | Serveur |
| Jackson | Liaison objets JAVA/JSON | Serveur -> Client |
| FileUtils | Upload de fichiers | Client -> Serveur |
| StringEscapeUtils | Transformation html/texte | Client -> Serveur |
| HTML/CSS | Standards du Web | Vues de l'application |
| Bootstrap | Mise en page aisée pour desktop et mobile avec un design efficace | Vues de l'application |
| Javascript (JQuery) | Solution simple, documentée et éprouvée pour rendre dynamique son application Web | Liaison modèle, requêtes Ajax, pages dynamiques | 
| EL Expression | Simple d'utilisation, pratique pour récupérer des éléments de contextes différents | Liaison modèle, au chargement des pages de l'application | 
| Dynatable (Plugin JQuery) | Tableau dynamiques simple et efficace  à l'utilisation | Ajout de membres / d'amis |

### Servlets et Pseudo REST

| Méthode HTTP | URI | Description
| :-: | :-: | :-: |
| TODO | TODO | TODO |

TODO

### Data Access Objects

**Screen MCD**

TODO : explication des DAOs et du DaoProvider

### L'objet Ticket

TODO : décrire cet objet et son utilité

### Difficultés techniques rencontrées et solutions apportées

TODO : cf le titre :) Peut-être sous forme d'un tableau "Problème/ Temps pour le régler / Commentaire"

### Conclusion

On peut donc conclure que l'utilisation de plusieurs technologies Web a permis de faciliter le développement tout en rendant l'application complète et efficace. 

---------------------

## Conclusion générale

TODO : Comparer les objectifs avec le résultat final + conclusion habituelle
