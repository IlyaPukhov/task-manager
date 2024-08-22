INSERT INTO users (id, username, firstname, lastname, birthdate, email, biography)
VALUES (1, 'norris', 'Chuck', 'Norris', '1940-01-01', 'r5Q9v@example.com', NULL),
       (2, 'brucelee', 'Bruce', 'Lee', '1940-11-27', 'brucelee@example.com', NULL),
       (3, 'stallone', 'Sylvester', 'Stallone', '1946-07-06', 'stallone@example.com', NULL);
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));