-- CATTEZ Edouard - FERRO Thomas
-- Create table

DROP TABLE IF EXISTS friend_with;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS belong_to;
DROP TABLE IF EXISTS subscriber;
DROP TABLE IF EXISTS discussion;
DROP TABLE IF EXISTS credential;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS subscriber_meta;
DROP TABLE IF EXISTS discussion_meta;

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

CREATE TABLE subscriber_meta(
  subscriber_meta_id SERIAL,
  picture TEXT,
  description TEXT,
  email TEXT,
  phone CHAR(10),
  CONSTRAINT pk_subscriber_meta PRIMARY KEY (subscriber_meta_id)
);

CREATE TABLE discussion_meta(
  discussion_meta_id SERIAL,
  description TEXT,
  CONSTRAINT pk_discussion_meta PRIMARY KEY (discussion_meta_id)
);

CREATE TABLE subscriber(
  subscriber_id SERIAL,
  subscriber_meta_id INTEGER,
  login VARCHAR(16) NOT NULL,
  first_name VARCHAR(64) NOT NULL,
  last_name VARCHAR(64) NOT NULL,
  birthdate DATE NOT NULL,
  CONSTRAINT pk_subscriber PRIMARY KEY (subscriber_id),
  CONSTRAINT fk_credential FOREIGN KEY (login) REFERENCES credential (login) ON UPDATE cascade,
  CONSTRAINT fk_subscriber_meta FOREIGN KEY (subscriber_meta_id) REFERENCES subscriber_meta (subscriber_meta_id) ON UPDATE cascade
);

CREATE TABLE friend_with(
  friend_1 INTEGER NOT NULL,
  friend_2 INTEGER NOT NULL,
  status BOOLEAN DEFAULT false,
  CONSTRAINT pk_friend_with PRIMARY KEY (friend_1, friend_2),
  CONSTRAINT fk_subscriber_1 FOREIGN KEY (friend_1) REFERENCES subscriber (subscriber_id) ON UPDATE cascade,
  CONSTRAINT fk_subscriber_2 FOREIGN KEY (friend_2) REFERENCES subscriber (subscriber_id) ON UPDATE cascade
);

CREATE TABLE discussion(
  discussion_id SERIAL,
  discussion_meta_id INTEGER,
  discussion_name VARCHAR(64) NOT NULL,
  enabled BOOLEAN DEFAULT false,
  CONSTRAINT pk_discussion PRIMARY KEY (discussion_id),
  CONSTRAINT fk_discussion_meta FOREIGN KEY (discussion_meta_id) REFERENCES discussion_meta (discussion_meta_id) ON UPDATE cascade
);

CREATE TABLE belong_to(
  subscriber_id INTEGER NOT NULL,
  discussion_id INTEGER NOT NULL,
  CONSTRAINT pk_belong_to PRIMARY KEY (subscriber_id, discussion_id),
  CONSTRAINT fk_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscriber (subscriber_id) ON UPDATE cascade,
  CONSTRAINT fk_discussion FOREIGN KEY (discussion_id) REFERENCES discussion (discussion_id) ON UPDATE cascade
);

CREATE TABLE message(
  message_id SERIAL,
  subscriber_id INTEGER NOT NULL,
  discussion_id INTEGER NOT NULL,
  content VARCHAR(512) NOT NULL,
  written_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_message PRIMARY KEY (message_id),
  CONSTRAINT fk_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscriber (subscriber_id) ON UPDATE cascade,
  CONSTRAINT fk_discussion FOREIGN KEY (discussion_id) REFERENCES discussion (discussion_id) ON UPDATE cascade
);
