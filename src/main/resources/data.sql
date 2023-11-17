INSERT INTO roles(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, description)
VALUES ('2023-11-15 00:00:00', 1, false, '2023-11-15 00:00:00', 1, 'Admin'),
       ('2023-11-15 00:00:00', 1, false, '2023-11-15 00:00:00', 1, 'Manager'),
       ('2023-11-15 00:00:00', 1, false, '2023-11-15 00:00:00', 1, 'Employee');

INSERT INTO users(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id, enabled,
                  first_name, last_name, user_name, gender, pass_word, phone, role_id)
VALUES ('2023-11-15 12:40:45', 1, false, '2023-11-15 12:40:45', 1, true, 'Mike', 'Smith', 'mike@admin.com', 'MALE',
        'abc1', '5537293101', 1),
       ('2023-11-15 15:37:02.552589', 1, false, '2023-11-15 15:37:02.552589', 1, true, 'John', 'Doe',
        'john@manager.com', 'MALE', 'abc1', '5574569988', 2),
       ('2023-11-15 15:39:02.552589', 1, false, '2023-11-15 15:39:02.552589', 1, true, 'Harold', 'Finch',
        'harold@manager.com', 'MALE', 'abc1', '5574563458', 2),
       ('2023-11-15 15:39:02.552589', 1, false, '2023-11-15 15:39:02.552589', 1, true, 'Sam', 'Ian', 'sam@employee.com',
        'MALE', 'abc1', '5574563576', 3),
       ('2023-11-15 15:39:02.552589', 1, false, '2023-11-15 15:39:02.552589', 1, true, 'Ozzy', 'Canlilar',
        'ozzy@employee.com', 'MALE', 'abc1', '5574563533', 3);


INSERT INTO projects(id, insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                     end_date, start_date, project_code, project_detail, project_name, project_status, manager_id)
VALUES (1, '2023-11-15 12:00:00', 1, false, '2023-11-15 12:00:00', 1, '2023-10-31', '2023-03-31', 'PROJ001',
        'Spring Core', 'Spring Core Project', 'OPEN', 2),
       (2, '2023-11-15 12:30:00', 1, false, '2023-11-15 12:30:00', 1, '2023-08-22', '2023-05-01', 'PROJ002',
        'Spring Boot', 'Spring Boot Project', 'IN_PROGRESS', 2),
       (3, '2023-11-15 13:00:00', 1, false, '2023-11-15 13:00:00', 1, '2023-12-31', '2023-10-10', 'PROJ003',
        'Spring MVC', 'Spring MVC Project', 'IN_PROGRESS', 3),
       (4, '2023-11-15 13:00:00', 1, false, '2023-11-15 13:00:00', 1, '2023-12-31', '2023-10-10', 'PROJ004',
        'Spring Data', 'Spring Data Project', 'COMPLETE', 3);

INSERT INTO tasks(insert_date_time, insert_user_id, is_deleted, last_update_date_time, last_update_user_id,
                  task_subject, task_detail, task_status, assigned_date, project_id, assigned_employee_id)
VALUES ('2023-11-15 13:00:00', 2, false, '2022-01-05 00:00:00', 2, 'Dependency Injection', 'Injecting dependencies',
        'OPEN', '2023-01-05', 1, 4),
       ('2023-11-15 13:00:00', 2, false, '2022-01-05 00:00:00', 2, '@SpringBootApplication',
        'Adding @SpringBootApplication annotation', 'IN_PROGRESS', '2023-01-05', 1, 4),
       ('2023-11-15 13:00:00', 2, false, '2022-01-05 00:00:00', 2, 'Controller', 'Creating controllers', 'COMPLETE',
        '2023-01-05', 1, 4),
       ('2023-11-15 13:00:00', 2, false, '2022-01-05 00:00:00', 2, 'Entity', 'Creating entities', 'OPEN', '2023-01-05',
        1, 4),
       ('2023-11-15 13:00:00', 2, false, '2022-01-05 00:00:00', 2, 'Dependency Injection', 'Injecting dependencies',
        'OPEN', '2023-01-05', 2, 5),
       ('2023-11-15 13:00:00', 2, false, '2022-01-05 00:00:00', 2, '@SpringBootApplication',
        'Adding @SpringBootApplication annotation', 'COMPLETE', '2023-01-05', 2, 5),
       ('2023-11-15 13:00:00', 2, false, '2022-01-05 00:00:00', 2, 'Controller', 'Creating controllers', 'IN_PROGRESS',
        '2023-01-05', 2, 5),
       ('2023-11-15 13:00:00', 3, false, '2022-01-05 00:00:00', 3, 'Entity', 'Creating entities', 'COMPLETE',
        '2023-01-05', 2, 5),
       ('2023-11-15 13:00:00', 3, false, '2022-01-05 00:00:00', 3, 'Dependency Injection', 'Injecting dependencies',
        'COMPLETE', '2023-01-05', 3, 4),
       ('2023-11-15 13:00:00', 3, false, '2022-01-05 00:00:00', 3, '@SpringBootApplication',
        'Adding @SpringBootApplication annotation', 'COMPLETE', '2023-01-05', 3, 4),
       ('2023-11-15 13:00:00', 3, false, '2022-01-05 00:00:00', 3, 'Controller', 'Creating controllers', 'IN_PROGRESS',
        '2023-01-05', 3, 4),
       ('2023-11-15 13:00:00', 3, false, '2022-01-05 00:00:00', 3, 'Entity', 'Creating entities', 'COMPLETE',
        '2023-01-05', 3, 5),
       ('2023-11-15 13:00:00', 3, false, '2022-01-05 00:00:00', 3, 'Dependency Injection', 'Injecting dependencies',
        'COMPLETE', '2023-01-05', 4, 5),
       ('2023-11-15 13:00:00', 3, false, '2022-01-05 00:00:00', 3, '@SpringBootApplication',
        'Adding @SpringBootApplication annotation', 'COMPLETE', '2023-01-05', 4, 5),
       ('2023-11-15 13:00:00', 3, false, '2022-01-05 00:00:00', 3, 'Controller', 'Creating controllers', 'COMPLETE',
        '2023-01-05', 4, 5),
       ('2023-11-15 13:00:00', 3, false, '2022-01-05 00:00:00', 3, 'Entity', 'Creating entities', 'COMPLETE',
        '2023-01-05', 4, 5);
