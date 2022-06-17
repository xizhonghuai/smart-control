-- truncate smart_control.role;
INSERT INTO smart_control.role (id, created_by, updated_by, name) VALUES (1, 1, 1, 'admin');
INSERT INTO smart_control.role (id, created_by, updated_by, name) VALUES (2, 1, 1, 'agent');
INSERT INTO smart_control.role (id, created_by, updated_by, name) VALUES (3, 1, 1, 'user');

-- truncate smart_control.account;
INSERT INTO smart_control.account (id, name, password, role_id) VALUES (1, 'admin', '123456', 1);

