-- Ferro Thomas
-- Liste de requêtes utiles - Projet Administration BDD - QuoiDNeuf
-- Remarque : Tests à changer selon utilisation

---------------------------------------------------------------------

-- Utilisateur :

--- Liste des utilisateurs :

SELECT login FROM subscriber;

--- Vérification si un utilisateur existe...

---- Par login :

SELECT 1 FROM subscriber WHERE login = 'ferrot';

---- Par nom :

SELECT 1 FROM subscriber WHERE last_name = 'Ferro';

---- Par prénom :

SELECT 1 FROM subscriber WHERE first_name = 'Thomas';

---- Par date de naissance :

SELECT 1 FROM subscriber WHERE birthdate = '19-10-1995';

--- Vérification mot de passe d'un utilisateur :

--- Récupération informations d'un utilisateur :

SELECT login, first_name, last_name, birthdate, picture, description, email, phone FROM subscriber AS s INNER JOIN subscriber_meta AS sm ON (s.subscriber_id = sm.subscriber_id) WHERE login = 'ferrot';

--- Liste amis d'un utilisateur :

SELECT sub.login FROM subscriber AS sub WHERE
  sub.subscriber_id IN (SELECT friend_2 FROM friend_with f INNER JOIN subscriber s ON (subscriber_id = friend_1) WHERE s.login = 'catteze')
  OR sub.subscriber_id IN (SELECT friend_1 FROM friend_with f INNER JOIN subscriber s ON (subscriber_id = friend_2) WHERE s.login = 'catteze');

--- Liste groupes d'un utilisateur :

SELECT discussion_name FROM belong_to AS b INNER JOIN discussion AS d ON (d.discussion_id = b.discussion_id) INNER JOIN subscriber AS s ON (s.subscriber_id = b.subscriber_id) WHERE login = 'fevrer';

--- Liste messages d'un utilisateur :

SELECT content, written_date FROM message AS m INNER JOIN subscriber AS s ON (s.subscriber_id = m.subscriber_id) WHERE login = 'ferrot' ORDER BY written_date ASC;

--- Vérifier si l'utilisateur est membre d'un groupe :

SELECT 1 FROM  belong_to AS b INNER JOIN subscriber AS s ON (s.subscriber_id = b.subscriber_id) INNER JOIN discussion AS d ON (d.discussion_id = b.discussion_id)
  WHERE login= 'ferrot' AND discussion_name = 'Nom de la discussion 1';

--- Récupérer rôle :
SELECT login, role FROM credential WHERE login = 'ferrot';


-- Groupe :

--- Liste des membres :

SELECT login FROM belong_to AS b INNER JOIN discussion AS d ON (d.discussion_id = b.discussion_id) INNER JOIN subscriber AS s ON (s.subscriber_id = b.subscriber_id)
  WHERE discussion_name = 'Nom de la discussion 1';

--- Vérifier si disponible :

SELECT enabled FROM discussion WHERE discussion_name = 'Nom de la discussion 1';

--- Liste des messages d'un groupe...

---- Tous les messages :

SELECT subscriber_id AS Envoyé_par, m.discussion_id AS Envoyé_vers, content AS Message, written_date AS Date FROM message AS m
  INNER JOIN discussion AS d ON (d.discussion_id = m.discussion_id)
  WHERE discussion_name = 'Nom de la discussion 1' ORDER BY written_date ASC;

---- Par jour :

SELECT subscriber_id AS Envoyé_par, m.discussion_id AS Envoyé_vers, content AS Message, written_date AS Date FROM message AS m
  INNER JOIN discussion AS d ON (d.discussion_id = m.discussion_id)
  WHERE discussion_name = 'Nom de la discussion 1'
  AND DATE(written_date) = '2016-01-09'
  ORDER BY written_date ASC;

---- D'un utilisateur donné :

SELECT m.subscriber_id AS Envoyé_par, m.discussion_id AS Envoyé_vers, content AS Message, written_date AS Date FROM message AS m
  INNER JOIN discussion AS d ON (d.discussion_id = m.discussion_id)
  INNER JOIN subscriber AS s ON (s.subscriber_id = m.subscriber_id)
  WHERE discussion_name = 'Nom de la discussion 1'
  AND login = 'ferrot'
  ORDER BY written_date ASC;

---- Par contenu :

SELECT subscriber_id AS Envoyé_par, m.discussion_id AS Envoyé_vers, content AS Message, written_date AS Date FROM message AS m
  INNER JOIN discussion AS d ON (d.discussion_id = m.discussion_id)
  WHERE content LIKE '%1%'
  AND discussion_name = 'Nom de la discussion 1' ORDER BY written_date ASC;

-- Amis :

--- Vérifier demandes d'ami :

SELECT friend_1, friend_2 FROM friend_with WHERE status = 'f';

--- Envoyer demande d'ami :

INSERT INTO friend_with (friend_1, friend_2)
  SELECT s1.subscriber_id, s2.subscriber_id FROM subscriber AS s1, subscriber AS s2 WHERE s1.login = 'scorsesem' AND s2.login = 'vincentf';

-- Accepter demande ami :

UPDATE friend_with SET status = 't'
  WHERE friend_1 = (SELECT subscriber_id FROM subscriber WHERE login = 'ferrot')
  OR friend_2 = (SELECT subscriber_id FROM subscriber WHERE login = 'ferrot');
