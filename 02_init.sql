-- CATTEZ Edouard - FERRO Thomas
-- Insert into tables

-- Role :
INSERT INTO role VALUES ('user');
INSERT INTO role VALUES ('super-user');
INSERT INTO role VALUES ('admin');

-- Credential :
INSERT INTO credential VALUES ('ferrot', 'e6c7781b1965a5788d333e9000fb2e1e', 'admin');
INSERT INTO credential VALUES ('catteze', 'c1e54c408022a92025fbe8db4a186945', 'admin');
INSERT INTO credential VALUES ('leleuj', 'd8bba3394e12b06dc98ab612e27f275d', 'super-user');
INSERT INTO credential VALUES ('fevrer', '2da154473efd1e5b9ed93aeb7339207f', 'super-user');
INSERT INTO credential VALUES ('katerinep', 'c1da15a8de58597f3034079cdaf5c326', 'user');
INSERT INTO credential VALUES ('vincentf', 'e8b539da0c9015b0379a1b153ea68d9d', 'user');
INSERT INTO credential VALUES ('vincentl', '1108027adcf4ae6a5879735f328e6d09', 'user');
INSERT INTO credential VALUES ('renk', '1108027adcf4ae6a5879735f328e6d09', 'user');
INSERT INTO credential VALUES ('jacksonsl', '43ddea0d5568d98cc525f5adc7321447', 'user');
INSERT INTO credential VALUES ('scorsesem', '3099ece3d8931fda8d907fd28de21234', 'user');

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

-- Discussion :
INSERT INTO discussion VALUES ('63d92b1e-c145-4e64-bd3a-e2a24d135d41', 'Nom de la discussion 1');
INSERT INTO discussion VALUES ('6783a5e5-27fe-4412-ba97-dc8d775891ac', 'Nom de la discussion 2');
INSERT INTO discussion VALUES ('352213d8-cec6-4ab6-9c41-815b5294d9af', 'Nom de la discussion 3');
INSERT INTO discussion VALUES ('16b2c067-fe4f-486d-8026-dcd7e10c885f', 'Nom de la discussion 4');
INSERT INTO discussion VALUES ('55c9a487-fc92-435d-bb19-727bc6e8d276', 'Nom de la discussion 5');
INSERT INTO discussion VALUES ('548b28ea-4e7b-4566-87b9-9ebe0c4778fb', 'Nom de la discussion 6');
INSERT INTO discussion VALUES ('9c40df67-6ad6-4e1d-8ec3-03b3e7df313c', 'Nom de la discussion 7');
INSERT INTO discussion VALUES ('c87f3598-ac2f-41c5-af77-4f28789c3b05', 'Nom de la discussion 8');
INSERT INTO discussion VALUES ('092fa7b6-0eca-43e3-be69-0999cb65cf16', 'Nom de la discussion 9');
INSERT INTO discussion VALUES ('e26bcb2a-0406-4950-aff9-c3d8d9611637', 'Nom de la discussion 10');

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
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('', 1, 1, 'Message test 1');
