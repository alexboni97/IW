-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, role, username, password)
VALUES (1, TRUE, 'ADMIN', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, role, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
-- < metido a mano
INSERT INTO IWUser (id, enabled, role, username, password)
VALUES (3, TRUE, 'ENTERPRISE', 'e',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
-- />
INSERT INTO IWAdmin (id, codigo_admin)
VALUES (1, 'ratatatatat');

INSERT INTO PARKER (id, first_Name, second_Name, DNI, telephone, email)
VALUES (2, 'Sergio', 'Sanchopanza', '12345678a', '123456789', 'elmejor@ucm.es');

INSERT INTO Enterprise (id, name, CIF, account_number, telephone)
VALUES (3, 'Enterprise 1', '12345678a', '12345678901234567890', '123456789');
-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;

INSERT INTO PARKING (id, name, address, telephone, email, enterprise_id, opening_time, closing_time, fee_per_hour,
enabled, latitude, longitude) VALUES (1, 'Parking 1', 'Calle de la piruleta', '123456789', 'parking1@gmail.com', 3, '08:00', '20:00', 1.5, TRUE, 40.416775, -3.703790);

INSERT INTO PARKING (id, name, address, telephone, email, enterprise_id, opening_time, closing_time, fee_per_hour,
enabled, latitude, longitude) VALUES (2, 'Parking 2', 'Calle de la fresa', '123456789', 'parking2@gmail.com', 3, '08:00', '20:00', 1.5, TRUE, 42.416775, -5.703790);
