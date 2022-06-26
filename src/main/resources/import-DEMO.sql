delete from cor_uaa_client;
delete from cor_uaa_role_user;
delete from cor_uaa_role;
delete from cor_uaa_user;

insert into cor_uaa_client (c_pk, c_created, c_ol, c_pid, c_authorized_grant_types, c_client_id, c_client_secret, c_scopes, c_web_server_redirect_uris, c_authentication_methods) values (1, now(), 0, '328e9a72-9f9e-4eef-86f4-917fb1f04b53', 'PASSWORD,REFRESH_TOKEN,AUTHORIZATION_CODE,CLIENT_CREDENTIALS','gateway', 'secret', 'openid,role.create,role.assign', 'http://127.0.0.1:8110/login/oauth2/code/gateway','CLIENT_SECRET_BASIC');

insert into core_uaa_client_setting (C_CLIENT_PK,C_KEY,C_VALUE) values (1,'require-authorization-consent','false');

-- Users
-- Password: tester
insert into cor_uaa_user (c_type, c_pk, c_created, c_ol, c_pid, c_enabled, c_expiration_date, c_extern, c_fullname, c_last_password_change, c_locked, c_password, c_comment, c_department, c_description, c_gender, c_im, c_image, c_office, c_phone_no, c_username) values ('STANDARD', 1, now(), 0, 'bb5efb8e-ad2a-427b-9f4c-83ec6e6c0e90', 'true', null, 'false', 'Testuser', now(), 'false', '$2a$15$k67lgzS8AaDOT9oKjY88qO1D9zpPY.AU.VSgIZ1fKgIaWjPVA55aC', 'Tester', 'Dep. 1', 'Just a test user', 'FEMALE', 'Skype:testee', null, 'Off. 815', '001-1234-56789', 'tester');
-- Password: mbinder
insert into cor_uaa_user (c_type, c_pk, c_created, c_ol, c_pid, c_enabled, c_expiration_date, c_extern, c_fullname, c_last_password_change, c_locked, c_password, c_comment, c_department, c_description, c_gender, c_im, c_image, c_office, c_phone_no, c_username) values ('STANDARD', 2, now(), 0, 'bb5efb8e-ad2a-427b-9f4c-83ec6e6c0e91', 'true', null, 'false', 'Heiko Scherrer', now(), 'false', '$2a$15$tXDkt7o/LSc1X51VklK6lOEvXt8RPFW/uN5mdchifE28tnUhXVkEC', 'Private account', 'Dep. 1', '', 'MALE', 'Skype:openwms', null, 'Off. 815', '001-1234-56789', 'hscherrer');

-- Roles
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('ROLE_Picking', 'ROLE', 'The Pickers role', true, 1, now(), 0, '1');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('ROLE_Receiving', 'ROLE', 'Operators in the Goods-In area', true, 2, now(), 0, '2');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('ROLE_Optimizer', 'ROLE', 'Operators care about stock optimization', true, 3, now(), 0, '3');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('ROLE_Shipping', 'ROLE', 'Operators in the Goods-Out area', true, 4, now(), 0, '4');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('ROLE_Counting', 'ROLE', 'Operators care about stock counting', true, 5, now(), 0, '5');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('ROLE_Useradministration', 'ROLE', 'Administration of Users', true, 6, now(), 0, '6');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('ROLE_Administrator', 'ROLE', 'Systemadministrator and Superuser', true, 7, now(), 0, '7');

-- Grants
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('role.create', 'GRANT', 'Create role', false, 20, now(), 0, '20');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('role.assign', 'GRANT', 'Assign role', false, 21, now(), 0, '21');

insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('inventory.view', 'GRANT', 'View inventory', false, 22, now(), 0, '22');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('inventory.check.view', 'GRANT', 'Verify inventory', false, 23, now(), 0, '23');

insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('movement.relocation.view', 'GRANT', 'Relocation movements', false, 24, now(), 0, '24');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('truck-list.view', 'GRANT', 'View truck lists', false, 25, now(), 0, '25');

insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('movement.view', 'GRANT', 'View movements', false, 26, now(), 0, '26');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('relocation.view', 'GRANT', 'View relocatiions', false, 27, now(), 0, '27');

insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('goods-in.view', 'GRANT', 'View receivings', false, 28, now(), 0, '28');

insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('picking.view', 'GRANT', 'View pick orders', false, 29, now(), 0, '29');
insert into cor_uaa_role (c_name, c_type, c_description, c_immutable, c_pk, c_created, c_ol, c_pid) values ('shipping.view', 'GRANT', 'View shipping orders', false, 30, now(), 0, '30');

-- Grant - Role assignments
-- Picking
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (1, 29);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (1, 30);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (1, 23);

-- Receiving
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (2, 28);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (2, 27);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (2, 23);

-- Optimization
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (3, 26);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (3, 27);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (3, 23);

-- Shipping
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (4, 24);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (4, 25);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (4, 23);

-- Counting
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (5, 22);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (5, 23);

-- User Administration
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 21);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 22);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 23);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 24);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 25);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 26);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 27);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 28);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 29);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (6, 30);

-- Administration
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 20);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 21);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 22);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 23);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 24);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 25);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 26);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 27);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 28);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 29);
insert into cor_uaa_role_role (c_role_id, c_grant_id) VALUES (7, 30);

-- User - Role assignments
insert into cor_uaa_role_user (c_role_id, c_user_id) VALUES (7, 2); -- Heiko
insert into cor_uaa_role_user (c_role_id, c_user_id) VALUES (1, 1); -- Tester
