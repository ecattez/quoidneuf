-- CATTEZ Edouard - FERRO Thomas
-- Create table

DROP TABLE IF EXISTS friend_with;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS belong_to;
DROP TABLE IF EXISTS subscriber;
DROP TABLE IF EXISTS discussion;
DROP TABLE IF EXISTS credential;
DROP TABLE IF EXISTS subscriber_meta;
DROP TABLE IF EXISTS discussion_meta;

CREATE TABLE credential(
  login VARCHAR(16),
  password_salt TEXT,
  password_hash TEXT,
  CONSTRAINT pk_credential PRIMARY KEY (login)
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
  login VARCHAR(16),
  first_name VARCHAR(64),
  last_name VARCHAR(64),
  birthday DATE,
  CONSTRAINT pk_subscriber PRIMARY KEY (subscriber_id),
  CONSTRAINT fk_credential FOREIGN KEY (login) REFERENCES credential (login) ON UPDATE cascade,
  CONSTRAINT fk_subscriber_meta FOREIGN KEY (subscriber_meta_id) REFERENCES subscriber_meta (subscriber_meta_id) ON UPDATE cascade
);

CREATE TABLE friend_with(
  friend_1 INTEGER,
  friend_2 INTEGER,
  CONSTRAINT pk_friend_with PRIMARY KEY (friend_1, friend_2),
  CONSTRAINT fk_subscriber_1 FOREIGN KEY (friend_1) REFERENCES subscriber (subscriber_id) ON UPDATE cascade,
  CONSTRAINT fk_subscriber_2 FOREIGN KEY (friend_2) REFERENCES subscriber (subscriber_id) ON UPDATE cascade
);

CREATE TABLE discussion(
  discussion_id SERIAL,
  discussion_meta_id INTEGER,
  discussion_name VARCHAR(64),
  enabled BOOLEAN,
  CONSTRAINT pk_discussion PRIMARY KEY (discussion_id),
  CONSTRAINT fk_discussion_meta FOREIGN KEY (discussion_meta_id) REFERENCES discussion_meta (discussion_meta_id) ON UPDATE cascade
);

CREATE TABLE belong_to(
  subscriber_id INTEGER,
  discussion_id INTEGER,
  CONSTRAINT pk_belong_to PRIMARY KEY (subscriber_id, discussion_id),
  CONSTRAINT fk_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscriber (subscriber_id) ON UPDATE cascade,
  CONSTRAINT fk_discussion FOREIGN KEY (discussion_id) REFERENCES discussion (discussion_id) ON UPDATE cascade
);

CREATE TABLE message(
  message_id SERIAL,
  subscriber_id INTEGER,
  discussion_id INTEGER,
  content VARCHAR(512),
  written_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT pk_message PRIMARY KEY (message_id),
  CONSTRAINT fk_subscriber FOREIGN KEY (subscriber_id) REFERENCES subscriber (subscriber_id) ON UPDATE cascade,
  CONSTRAINT fk_discussion FOREIGN KEY (discussion_id) REFERENCES discussion (discussion_id) ON UPDATE cascade
);
