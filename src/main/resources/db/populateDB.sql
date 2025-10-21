DELETE FROM user_role;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2022-10-30 21:00', 'ужин', 1000, 100001),
       ('2022-10-29 10:00', 'обед', 1300, 100001),
       ('2022-10-29 13:00', 'обед', 500, 100001),
       ('2022-10-29 19:00', 'обед', 500, 100001),
       ('2022-10-31 9:00', 'завтрак', 500, 100000),
       ('2022-10-31 12:00', 'обед', 500, 100000),
       ('2022-10-31 21:00', 'ужин', 1200, 100000),
       ('2022-10-30 12:00', 'обед', 500, 100000),
       ('2022-10-30 21:00', 'ужин', 1200, 100000);