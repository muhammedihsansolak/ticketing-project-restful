INSERT INTO roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, description)
VALUES ('2023-11-15 00:00:00', 1, false, '2023-11-15 00:00:00', 1, 'Admin'),
       ('2023-11-15 00:00:00', 1, false, '2023-11-15 00:00:00', 1, 'Manager'),
       ('2023-11-15 00:00:00', 1, false, '2023-11-15 00:00:00', 1, 'Employee');

INSERT INTO users(id, insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, enabled, first_name, last_name, user_name, gender, pass_word, phone, role_id)
VALUES (1, '2023-11-15 12:40:45', 1, false, '2023-11-15 12:40:45', 1, true, 'Mike', 'Smith', 'mike@admin.com', 'MALE', 'abc1', '5537293101',1),
       (2, '2023-11-15 15:37:02.552589', 1, false, '2023-11-15 15:37:02.552589', 1, true, 'John', 'Doe', 'John@manager.com', 'MALE', 'abc1', '5574569988',2),
       (3, '2023-11-15 15:39:02.552589', 1, false, '2023-11-15 15:39:02.552589', 1, true, 'Harold', 'Finch', 'Harold@manager.com', 'MALE', 'abc1', '5574563458',2),
       (4, '2023-11-15 15:39:02.552589', 1, false, '2023-11-15 15:39:02.552589', 1, true, 'Sam', 'Ian', 'Sam@employee.com', 'MALE', 'abc1', '5574563576',3),
       (5, '2023-11-15 15:39:02.552589', 1, false, '2023-11-15 15:39:02.552589', 1, true, 'Ozzy', 'Canlilar', 'Ozzy@employee.com', 'MALE', 'abc1', '5574563533',3);