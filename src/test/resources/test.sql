insert into cor_uaa_user (c_type, c_pk, c_created, c_updated, c_ol, c_pid, c_enabled, c_expiration_date, c_extern, c_fullname, c_last_password_change, c_locked, c_password, c_comment, c_department, c_description, c_gender, c_im, c_image, c_office, c_phone_no, c_username) values ('STANDARD', 1, '2020-06-22 19:02:47.404000', '2020-06-22 19:02:47.404000', 0, '96baa849-dd19-4b19-8c5e-895d3b7f405d', 'true', '2020-06-23 19:02:45.054756', 'false', 'Mister Jenkins', '2020-06-22 19:02:47.330440', 'false', '$2a$15$baURCfRsoxem.eOv0IJDsup.9wEmHdiw.j8f0RaMflDbFnQWNipvG', 'testing only', 'Dep. 1', 'Just a test user', 'FEMALE', 'Skype:testee', null, 'Off. 815', '001-1234-56789', 'tester');

insert into cor_uaa_email (c_pk, c_created, c_updated, c_ol, c_address, c_full_name, c_primary, c_username) values (2, '2020-06-22 19:02:47.425000', '2020-06-22 19:02:47.425000', 0, 'tester.tester@example.com', null, 'true', 'tester');

INSERT INTO COR_UAA_CLIENT_DETAILS (C_PK, C_PID, C_CREATED, C_OL, C_CLIENT_ID, C_CLIENT_SECRET, C_SCOPE, C_AUTHORIZED_GRANT_TYPES, C_WEB_SERVER_REDIRECT_URI, C_AUTHORITIES, C_ACCESS_TOKEN_VALIDITY,C_REFRESH_TOKEN_VALIDITY, C_ADDITIONAL_INFORMATION, C_AUTOAPPROVE) VALUES (1000, '1000', now(), 0, 'gateway', 'secret', 'gateway', 'password,authorization_code,refresh_token,implicit', 'http://localhost:8086/login/oauth2/code/gateway', null, 36000, 36000, null, true);

INSERT INTO COR_UAA_ROLE (c_type, c_pk, c_created, c_ol, c_pid, c_description, c_name, c_immutable) VALUES ('ROLE', 1, now(), 0, '1', 'Super user role', 'ROLE_ADMIN', true);
INSERT INTO COR_UAA_ROLE (c_type, c_pk, c_created, c_ol, c_pid, c_description, c_name, c_immutable) VALUES ('ROLE', 2, now(), 0, '2', 'Operator role', 'ROLE_OPS', true);
INSERT INTO COR_UAA_ROLE (c_type, c_pk, c_created, c_ol, c_pid, c_description, c_name, c_immutable) VALUES ('GRANT', 3, now(), 0, '3', 'Permission to find Users', 'SEC_UAA_USER_LOOKUP', false);
INSERT INTO COR_UAA_ROLE (c_type, c_pk, c_created, c_ol, c_pid, c_description, c_name, c_immutable) VALUES ('GRANT', 4, now(), 0, '4', 'Permission to create Users', 'SEC_UAA_USER_CREATE', false);
INSERT INTO COR_UAA_ROLE (c_type, c_pk, c_created, c_ol, c_pid, c_description, c_name, c_immutable) VALUES ('GRANT', 5, now(), 0, '5', 'Permission to modify Users', 'SEC_UAA_USER_MODIFY', false);
INSERT INTO COR_UAA_ROLE (c_type, c_pk, c_created, c_ol, c_pid, c_description, c_name, c_immutable) VALUES ('GRANT', 6, now(), 0, '6', 'Permission to delete Users', 'SEC_UAA_USER_DELETE', false);


