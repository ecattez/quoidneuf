-- CATTEZ Edouard - FERRO Thomas
-- Create table

DROP VIEW IF EXISTS discussion_trie;
DROP VIEW IF EXISTS subscriber_message_discussion;
DROP TABLE IF EXISTS friend_with;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS belong_to;
DROP TABLE IF EXISTS subscriber_meta;
DROP TABLE IF EXISTS subscriber;
DROP TABLE IF EXISTS discussion;
DROP TABLE IF EXISTS credential;
DROP TABLE IF EXISTS role;

CREATE TABLE role(
  role TEXT NOT NULL,
  CONSTRAINT pk_role PRIMARY KEY (role)
);

CREATE TABLE credential(
  login VARCHAR(16) NOT NULL,
  password_hash TEXT NOT NULL,
  role TEXT NOT NULL,
  CONSTRAINT pk_credential PRIMARY KEY (login),
  CONSTRAINT fk_role FOREIGN KEY (role) REFERENCES role (role) ON UPDATE cascade
);

CREATE TABLE subscriber(
  subscriber_id SERIAL,
  login VARCHAR(16),
  first_name VARCHAR(64) NOT NULL,
  last_name VARCHAR(64) NOT NULL,
  birthday DATE NOT NULL,
  CONSTRAINT pk_subscriber PRIMARY KEY (subscriber_id),
  CONSTRAINT fk_credential FOREIGN KEY (login) REFERENCES credential (login) ON UPDATE cascade ON DELETE SET NULL
);

CREATE TABLE subscriber_meta(
  subscriber_meta_id SERIAL,
  subscriber_id INTEGER,
  picture TEXT DEFAULT 'img/default-profile.png',
  description TEXT,
  email TEXT NOT NULL,
  phone CHAR(10),
  CONSTRAINT pk_subscriber_meta PRIMARY KEY (subscriber_meta_id),
  CONSTRAINT fk_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscriber (subscriber_id) ON UPDATE cascade
);

CREATE TABLE friend_with(
  friend_1 INTEGER NOT NULL,
  friend_2 INTEGER NOT NULL,
  status INTEGER,
  CONSTRAINT pk_friend_with PRIMARY KEY (friend_1, friend_2),
  CONSTRAINT fk_subscriber_1 FOREIGN KEY (friend_1) REFERENCES subscriber (subscriber_id) ON UPDATE cascade,
  CONSTRAINT fk_subscriber_2 FOREIGN KEY (friend_2) REFERENCES subscriber (subscriber_id) ON UPDATE cascade
);

CREATE TABLE discussion(
  discussion_id CHAR(36),
  discussion_name VARCHAR(64) NOT NULL,
  CONSTRAINT pk_discussion PRIMARY KEY (discussion_id)
);

CREATE TABLE belong_to(
  subscriber_id INTEGER NOT NULL,
  discussion_id CHAR(36) NOT NULL,
  CONSTRAINT pk_belong_to PRIMARY KEY (subscriber_id, discussion_id),
  CONSTRAINT fk_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscriber (subscriber_id) ON UPDATE cascade,
  CONSTRAINT fk_discussion FOREIGN KEY (discussion_id) REFERENCES discussion (discussion_id) ON UPDATE cascade
);

CREATE TABLE message(
  message_id VARCHAR(36),
  subscriber_id INTEGER NOT NULL,
  discussion_id CHAR(36) NOT NULL,
  content VARCHAR(512) NOT NULL,
  written_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_message PRIMARY KEY (message_id),
  CONSTRAINT fk_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscriber (subscriber_id) ON UPDATE cascade,
  CONSTRAINT fk_discussion FOREIGN KEY (discussion_id) REFERENCES discussion (discussion_id) ON UPDATE cascade
);

CREATE VIEW discussion_trie AS
  SELECT discussion_id AS _to, discussion_name AS _name, written_date AS _date_last_message
  FROM discussion AS d INNER JOIN message AS m USING(discussion_id)
  GROUP BY d.discussion_id, m.message_id HAVING written_date >= ALL (SELECT written_date FROM message WHERE discussion_id = d.discussion_id)
  ORDER BY written_date DESC;

CREATE VIEW subscriber_message_discussion AS
  SELECT s.subscriber_id as _from, s.first_name as _first_name, s.last_name as _last_name, d.discussion_id as _to, d.discussion_name as _name, content as _content, written_date as _date
	FROM discussion d LEFT JOIN message m ON (d.discussion_id = m.discussion_id)
	LEFT JOIN subscriber s ON (m.subscriber_id = s.subscriber_id);
	
-- Role :
INSERT INTO role VALUES ('user');
INSERT INTO role VALUES ('super-user');
INSERT INTO role VALUES ('admin');
