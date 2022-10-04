delete from COR_UAA_USER_PASSWORD;
delete from COR_UAA_ROLE_ROLE;
delete from COR_UAA_ROLE_USER;
delete from COR_UAA_ROLE;
delete from COR_UAA_CLIENT;
delete from COR_UAA_EMAIL;
delete from COR_UAA_USER;

insert into COR_UAA_USER (C_TYPE, C_PK, C_CREATED, C_OL, C_PID, C_ENABLED, C_EXPIRATION_DATE, C_EXTERN, C_FULLNAME, C_LAST_PASSWORD_CHANGE, C_LOCKED, C_PASSWORD, C_COMMENT, C_DEPARTMENT, C_DESCRIPTION, C_GENDER, C_IM, C_IMAGE, C_OFFICE, C_PHONE_NO, C_USERNAME) values ('STANDARD', 1000, '2020-06-22 19:02:47.404000', 1, '96baa849-dd19-4b19-8c5e-895d3b7f405d', true, '2020-06-23 19:02:45.054756', true, 'Mister Jenkins', '2020-06-22 19:02:47.330440', true, '{bcrypt}$2a$15$baURCfRsoxem.eOv0IJDsup.9wEmHdiw.j8f0RaMflDbFnQWNipvG', 'Test administrator', 'Dep. 0', 'A virtual one', 'MALE', 'Skype:admine', null, 'Off. 4711', '001-1234-98765', 'jenkins');
insert into COR_UAA_USER (C_TYPE, C_PK, C_CREATED, C_OL, C_PID, C_ENABLED, C_EXPIRATION_DATE, C_EXTERN, C_FULLNAME, C_LAST_PASSWORD_CHANGE, C_LOCKED, C_PASSWORD, C_COMMENT, C_DEPARTMENT, C_DESCRIPTION, C_GENDER, C_IM, C_IMAGE, C_OFFICE, C_PHONE_NO, C_USERNAME) values ('STANDARD', 1001, '2020-06-22 19:02:47.404000', 1, '96baa849-dd19-4b19-8c5e-895d3b7f405e', true, '2020-06-23 19:02:45.054756', false, 'Tester', '2020-06-22 19:02:47.330440', false, '{bcrypt}$2a$15$baURCfRsoxem.eOv0IJDsup.9wEmHdiw.j8f0RaMflDbFnQWNipvG', 'testing only', 'Dep. 1', 'Just a test user', 'FEMALE', 'Skype:testee', null, 'Off. 815', '001-1234-56789', 'tester');

insert into COR_UAA_EMAIL (C_PK, C_CREATED, C_OL, C_ADDRESS, C_FULL_NAME, C_PRIMARY, C_USER_PK) values (1000, '2020-06-22 19:02:39.000000', 1, 'admin.private@acme.com', 'Mr. Jenkins', true, 1000);
insert into COR_UAA_EMAIL (C_PK, C_CREATED, C_OL, C_ADDRESS, C_FULL_NAME, C_PRIMARY, C_USER_PK) values (1001, '2020-06-22 19:02:40.000000', 1, 'admin@acme.com', 'Mr. Jenkins', false, 1000);
insert into COR_UAA_EMAIL (C_PK, C_CREATED, C_OL, C_ADDRESS, C_FULL_NAME, C_PRIMARY, C_USER_PK) values (1002, '2020-06-22 19:02:41.000000', 1, 'tester@acme.com', 'Mr. Tester', true, 1001);

insert into COR_UAA_CLIENT (C_PK, C_PID, C_CREATED, C_OL, C_CLIENT_ID, C_CLIENT_SECRET, C_SCOPES, C_AUTHORIZED_GRANT_TYPES, C_WEB_SERVER_REDIRECT_URIS) VALUES (1000, '1000', now(), 1, 'gateway', 'secret', 'gateway', 'password,authorization_code,refresh_token,implicit', 'http://localhost:8086/login/oauth2/code/gateway');

insert into COR_UAA_ROLE (C_TYPE, C_PK, C_CREATED, C_OL, C_PID, C_DESCRIPTION, C_NAME, C_IMMUTABLE) VALUES ('ROLE', 1000, now(), 1, '1', 'Super user role', 'ROLE_ADMIN', true);
insert into COR_UAA_ROLE (C_TYPE, C_PK, C_CREATED, C_OL, C_PID, C_DESCRIPTION, C_NAME, C_IMMUTABLE) VALUES ('ROLE', 1001, now(), 1, '2', 'Operator role', 'ROLE_OPS', true);
insert into COR_UAA_ROLE (C_TYPE, C_PK, C_CREATED, C_OL, C_PID, C_DESCRIPTION, C_NAME, C_IMMUTABLE) VALUES ('GRANT', 1002, now(), 1, '3', 'Permission to find Users', 'SEC_UAA_USER_LOOKUP', false);
insert into COR_UAA_ROLE (C_TYPE, C_PK, C_CREATED, C_OL, C_PID, C_DESCRIPTION, C_NAME, C_IMMUTABLE) VALUES ('GRANT', 1003, now(), 1, '4', 'Permission to create Users', 'SEC_UAA_USER_CREATE', false);
insert into COR_UAA_ROLE (C_TYPE, C_PK, C_CREATED, C_OL, C_PID, C_DESCRIPTION, C_NAME, C_IMMUTABLE) VALUES ('GRANT', 1004, now(), 1, '5', 'Permission to modify Users', 'SEC_UAA_USER_MODIFY', false);
insert into COR_UAA_ROLE (C_TYPE, C_PK, C_CREATED, C_OL, C_PID, C_DESCRIPTION, C_NAME, C_IMMUTABLE) VALUES ('GRANT', 1005, now(), 1, '6', 'Permission to delete Users', 'SEC_UAA_USER_DELETE', false);

insert into COR_UAA_ROLE_USER (C_ROLE_ID, C_USER_ID) VALUES (1000, 1000);

insert into COR_UAA_ROLE_ROLE (C_ROLE_ID, C_GRANT_ID) VALUES (1000, 1002);
insert into COR_UAA_ROLE_ROLE (C_ROLE_ID, C_GRANT_ID) VALUES (1000, 1003);
insert into COR_UAA_ROLE_ROLE (C_ROLE_ID, C_GRANT_ID) VALUES (1000, 1004);
insert into COR_UAA_ROLE_ROLE (C_ROLE_ID, C_GRANT_ID) VALUES (1000, 1005);