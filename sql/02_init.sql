-- CATTEZ Edouard - FERRO Thomas
-- Insert into tables

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
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('ferrot', 'Thomas', 'Ferro', '19-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('catteze', 'Edouard', 'Cattez', '20-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('leleuj', 'Julien', 'Leleu', '21-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('fevrer', 'Remy', 'Fevre', '22-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('katerinep', 'Philippe', 'Katerine', '23-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('vincentf', 'Francky', 'Vincent', '24-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('vincentl', 'Lagaff', 'Vincent', '25-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('renk', 'Kylo', 'Ren', '26-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('jacksonsl', 'Samuel L', 'Jackson', '27-10-1995');
INSERT INTO subscriber (login, first_name, last_name, birthday) VALUES ('scorsesem', 'Martin', 'Scorsese', '28-10-1995');

-- Subscriber_meta :
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (1, 'Description test', '1@mail.com', '1111111111');
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (2,'Description test', '2@mail.com', '2222222222');
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (3,'Description test', '3@mail.com', '3333333333');
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (4,'Description test', '4@mail.com', '4444444444');
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (5,'Description test', '5@mail.com', '5555555555');
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (6,'Description test', '6@mail.com', '6666666666');
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (7,'Description test', '7@mail.com', '7777777777');
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (8,'Description test', '8@mail.com', '8888888888');
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (9,'Description test', '9@mail.com', '9999999999');
INSERT INTO subscriber_meta (subscriber_id, description, email, phone) VALUES (10,'Description test', '10@mail.com', '1010101010');

-- Friend_with :
INSERT INTO friend_with VALUES (1, 2, 1);
INSERT INTO friend_with VALUES (1, 3, 1);
INSERT INTO friend_with VALUES (1, 4, 4);
INSERT INTO friend_with VALUES (1, 5, 0);
INSERT INTO friend_with VALUES (1, 6, 6);
INSERT INTO friend_with VALUES (1, 7, 7);
INSERT INTO friend_with VALUES (1, 8, 1);
INSERT INTO friend_with VALUES (1, 9, 0);
INSERT INTO friend_with VALUES (1, 10, 1);
INSERT INTO friend_with VALUES (2, 3, 2);
INSERT INTO friend_with VALUES (2, 4, 4);
INSERT INTO friend_with VALUES (2, 5, 2);
INSERT INTO friend_with VALUES (2, 6, 0);
INSERT INTO friend_with VALUES (2, 7, 7);
INSERT INTO friend_with VALUES (2, 8, 2);
INSERT INTO friend_with VALUES (2, 9, 9);
INSERT INTO friend_with VALUES (2, 10, 0);
INSERT INTO friend_with VALUES (3, 4, 3);
INSERT INTO friend_with VALUES (3, 5, 5);
INSERT INTO friend_with VALUES (3, 6, 6);

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
INSERT INTO belong_to VALUES (1, '63d92b1e-c145-4e64-bd3a-e2a24d135d41');
INSERT INTO belong_to VALUES (1, '6783a5e5-27fe-4412-ba97-dc8d775891ac');
INSERT INTO belong_to VALUES (1, '352213d8-cec6-4ab6-9c41-815b5294d9af');
INSERT INTO belong_to VALUES (1, '16b2c067-fe4f-486d-8026-dcd7e10c885f');
INSERT INTO belong_to VALUES (1, '55c9a487-fc92-435d-bb19-727bc6e8d276');
INSERT INTO belong_to VALUES (1, '548b28ea-4e7b-4566-87b9-9ebe0c4778fb');
INSERT INTO belong_to VALUES (2, '9c40df67-6ad6-4e1d-8ec3-03b3e7df313c');
INSERT INTO belong_to VALUES (2, 'c87f3598-ac2f-41c5-af77-4f28789c3b05');
INSERT INTO belong_to VALUES (2, '092fa7b6-0eca-43e3-be69-0999cb65cf16');
INSERT INTO belong_to VALUES (2, 'e26bcb2a-0406-4950-aff9-c3d8d9611637');
INSERT INTO belong_to VALUES (3, '63d92b1e-c145-4e64-bd3a-e2a24d135d41');
INSERT INTO belong_to VALUES (3, '6783a5e5-27fe-4412-ba97-dc8d775891ac');
INSERT INTO belong_to VALUES (3, '352213d8-cec6-4ab6-9c41-815b5294d9af');
INSERT INTO belong_to VALUES (3, '16b2c067-fe4f-486d-8026-dcd7e10c885f');
INSERT INTO belong_to VALUES (4, '55c9a487-fc92-435d-bb19-727bc6e8d276');
INSERT INTO belong_to VALUES (4, '548b28ea-4e7b-4566-87b9-9ebe0c4778fb');
INSERT INTO belong_to VALUES (4, '9c40df67-6ad6-4e1d-8ec3-03b3e7df313c');
INSERT INTO belong_to VALUES (4, 'c87f3598-ac2f-41c5-af77-4f28789c3b05');
INSERT INTO belong_to VALUES (4, '092fa7b6-0eca-43e3-be69-0999cb65cf16');
INSERT INTO belong_to VALUES (4, 'e26bcb2a-0406-4950-aff9-c3d8d9611637');

-- Message :
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('34502966-cacc-4b58-9b57-cd29cb372f92', 1, '63d92b1e-c145-4e64-bd3a-e2a24d135d41', 'Message test 1');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('f91bb91c-8de0-4983-9f99-8b21c5619a63', 1, '6783a5e5-27fe-4412-ba97-dc8d775891ac', 'Message test 2');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('5d344b4b-512b-42c6-adcc-6d2e41b0f6bc', 1, '352213d8-cec6-4ab6-9c41-815b5294d9af', 'Message test 3');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('aff29eb7-da5b-46ef-87a9-bddc8bbdb766', 1, '16b2c067-fe4f-486d-8026-dcd7e10c885f', 'Message test 4');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('4c22502d-d65e-467c-a12a-69885cc18f45', 1, '55c9a487-fc92-435d-bb19-727bc6e8d276', 'Message test 5');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('728e4fbc-c9ae-400e-a7bb-98df0ef9fd94', 1, '548b28ea-4e7b-4566-87b9-9ebe0c4778fb', 'Message test 6');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('3a7a143e-c526-495a-b7c7-e2db428ea035', 1, '9c40df67-6ad6-4e1d-8ec3-03b3e7df313c', 'Message test 7');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('f3743c6f-0438-4675-9d75-92d8c38e9f8b', 1, 'c87f3598-ac2f-41c5-af77-4f28789c3b05', 'Message test 8');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('dea7861d-dd5c-41ba-91c2-2e8ac7af9c6f', 1, '092fa7b6-0eca-43e3-be69-0999cb65cf16', 'Message test 9');
INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES ('2d775568-ae44-4d57-8609-756dfe65c115', 1, 'e26bcb2a-0406-4950-aff9-c3d8d9611637', 'Message test 10');
