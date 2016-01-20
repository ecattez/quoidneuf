-- CATTEZ Edouard - FERRO Thomas
-- Insert into tables

-- Role :
INSERT INTO role VALUES ('user');
INSERT INTO role VALUES ('super-user');
INSERT INTO role VALUES ('admin');

-- Credential :
INSERT INTO credential VALUES ('ferrot', 'passhashferrot', 'admin');
INSERT INTO credential VALUES ('catteze', 'passhashcatteze', 'admin');
INSERT INTO credential VALUES ('leleuj', 'passhashleleuj', 'super-user');
INSERT INTO credential VALUES ('fevrer', 'passhashfevrer', 'super-user');
INSERT INTO credential VALUES ('katerinep', 'passhashketrinep', 'user');
INSERT INTO credential VALUES ('vincentf', 'passhashvincenf', 'user');
INSERT INTO credential VALUES ('vincentl', 'passhashvincentl', 'user');
INSERT INTO credential VALUES ('renk', 'passhashrenk', 'user');
INSERT INTO credential VALUES ('jacksonsl', 'passhashjacksonsl', 'user');
INSERT INTO credential VALUES ('scorsesem', 'passhashscorsesem', 'user');

-- Subscriber :
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('ferrot', 'Thomas', 'Ferro', '19-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('catteze', 'Edouard', 'Cattez', '20-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('leleuj', 'Julien', 'Leleu', '21-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('fevrer', 'Remy', 'Fevre', '22-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('katerinep', 'Philippe', 'Katerine', '23-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('vincentf', 'Francky', 'Vincent', '24-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('vincentl', 'Lagaff', 'Vincent', '25-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('renk', 'Kylo', 'Ren', '26-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('jacksonsl', 'Samuel L', 'Jackson', '27-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthdate) VALUES ('scorsesem', 'Martin', 'Scorsese', '28-10-1995');

-- Subscriber_meta :
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (1, 'picturepath', 'Description test', '1@mail.com', '1111111111');
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (2,'picturepath', 'Description test', '2@mail.com', '2222222222');
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (3,'picturepath', 'Description test', '3@mail.com', '3333333333');
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (4,'picturepath', 'Description test', '4@mail.com', '4444444444');
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (5,'picturepath', 'Description test', '5@mail.com', '5555555555');
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (6,'picturepath', 'Description test', '6@mail.com', '6666666666');
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (7,'picturepath', 'Description test', '7@mail.com', '7777777777');
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (8,'picturepath', 'Description test', '8@mail.com', '8888888888');
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (9,'picturepath', 'Description test', '9@mail.com', '9999999999');
INSERT INTO subscriber_meta (subscriber_id, picture, description, email, phone) VALUES (10,'picturepath', 'Description test', '10@mail.com', '1010101010');

-- Discussion :
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 1');
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 2');
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 3');
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 4');
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 5');
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 6');
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 7');
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 8');
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 9');
INSERT INTO discussion (discussion_name) VALUES ('Nom de la discussion 10');

-- Friend_with :
INSERT INTO friend_with VALUES (1, 2);
INSERT INTO friend_with VALUES (1, 3);
INSERT INTO friend_with VALUES (1, 4);
INSERT INTO friend_with VALUES (1, 5);
INSERT INTO friend_with VALUES (1, 6);
INSERT INTO friend_with VALUES (1, 7);
INSERT INTO friend_with VALUES (1, 8);
INSERT INTO friend_with VALUES (1, 9);
INSERT INTO friend_with VALUES (1, 10);
INSERT INTO friend_with VALUES (2, 3);
INSERT INTO friend_with VALUES (2, 4);
INSERT INTO friend_with VALUES (2, 5);
INSERT INTO friend_with VALUES (2, 6);
INSERT INTO friend_with VALUES (2, 7);
INSERT INTO friend_with VALUES (2, 8);
INSERT INTO friend_with VALUES (2, 9);
INSERT INTO friend_with VALUES (2, 10);
INSERT INTO friend_with VALUES (3, 4);
INSERT INTO friend_with VALUES (3, 5);
INSERT INTO friend_with VALUES (3, 6);

-- Belong_to :
INSERT INTO belong_to VALUES (1, 1);
INSERT INTO belong_to VALUES (1, 2);
INSERT INTO belong_to VALUES (1, 3);
INSERT INTO belong_to VALUES (1, 4);
INSERT INTO belong_to VALUES (1, 5);
INSERT INTO belong_to VALUES (1, 6);
INSERT INTO belong_to VALUES (2, 7);
INSERT INTO belong_to VALUES (2, 8);
INSERT INTO belong_to VALUES (2, 9);
INSERT INTO belong_to VALUES (2, 10);
INSERT INTO belong_to VALUES (2, 1);
INSERT INTO belong_to VALUES (2, 2);
INSERT INTO belong_to VALUES (3, 3);
INSERT INTO belong_to VALUES (3, 4);
INSERT INTO belong_to VALUES (3, 5);
INSERT INTO belong_to VALUES (3, 6);
INSERT INTO belong_to VALUES (3, 7);
INSERT INTO belong_to VALUES (3, 8);
INSERT INTO belong_to VALUES (4, 9);
INSERT INTO belong_to VALUES (4, 10);

-- Message :
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 1', 1, 1);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 2', 2, 3);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 3', 4, 1);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 4', 1, 2);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 5', 9, 10);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 6', 5, 3);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 7', 1, 1);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 8', 9, 1);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 9', 4, 5);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 10', 6, 2);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 11', 7, 1);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 12', 3, 8);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 13', 7, 1);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 14', 1, 5);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 15', 3, 5);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 16', 1, 5);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 17', 4, 5);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 18', 1, 6);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 19', 7, 9);
INSERT INTO message (content, subscriber_id, discussion_id) VALUES ('Message test 20', 4, 6);
