ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
-- insert admin (username a, password aa)
/*INSERT INTO IWUser (id, enabled, role, username, password)
VALUES (1, TRUE, 'ADMIN', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
INSERT INTO IWUser (id, enabled, role, username, password)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

-- INSERT INTO IWUser (id, enabled, role, username, password)
-- VALUES (4, TRUE, 'USER', 'c',
--     '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
-- < metido a mano
INSERT INTO IWUser (id, enabled, role, username, password)
VALUES (3, TRUE, 'ENTERPRISE', 'e',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');
-- />
INSERT INTO IWAdmin (id, codigo_admin)
VALUES (1, 'ratatatatat');

INSERT INTO PARKER (id, first_Name, second_Name, DNI, telephone, email, wallet)
VALUES (1, 'Sergio', 'Sanchopanza', '12345678a', '123456789', 'elmejor@ucm.es', 150.00);

INSERT INTO PARKER (id, first_Name, second_Name, DNI, telephone, email, wallet)
VALUES (2, 'Paula', 'ayuda', '12345678b', '123456780', 'lamejor@ucm.es', 200.00);
INSERT INTO Enterprise (id, name, CIF, account_number, telephone)
VALUES (3, 'Enterprise 1', '12345678a', '12345678901234567890', '123456789');

-- start id numbering from a value that is larger than any assigned above

INSERT INTO PARKING (id, name, address, cp, city, country, telephone, email, enterprise_id, opening_time, closing_time, fee_per_hour,
enabled, latitude, longitude) VALUES (1, 'Parking 1', 'Calle de la piruleta', 28005, 'Madrid', 'Espanya', '123456789', 'parking1@gmail.com', 3, '08:00', '20:00', 1.5, TRUE, 40.416775, -3.703790);

INSERT INTO PARKING (id, name, address, cp, city, country, telephone, email, enterprise_id, opening_time, closing_time, fee_per_hour,
enabled, latitude, longitude) VALUES (2, 'Parking 2', 'Calle de la fresa', 28005, 'Madrid', 'Espanya', '123456789', 'parking2@gmail.com', 3, '08:00', '20:00', 1.5, TRUE, 42.416775, -5.703790);

INSERT INTO PARKING (id, name, address, cp, city, country, telephone, email, enterprise_id, opening_time, closing_time, fee_per_hour,
enabled, latitude, longitude) VALUES (3, 'Parking 3', 'Paseo de los Olmos', 28005, 'Madrid', 'Espanya', '123456789', 'parking3@gmail.com', 3, '08:00', '20:00', 1.5, TRUE, 40.404711, -3.710862);

INSERT INTO SPOT (id, enabled, type, size, coords, parking_id)
VALUES (1, TRUE, 'COMPACT', '2.5x5', '40.416775,-3.703790', 1);

INSERT INTO SPOT (id, enabled, type, size, coords, parking_id)
VALUES (2, TRUE, 'COMPACT', '2.5x5', '40.416775,-3.703790', 2);

INSERT INTO Vehicle (id, enabled, plate, brand, model, size, parker_id)
VALUES (1, true, 'ABC1234', 'Toyota', 'Corolla', 'M', 1);

INSERT INTO Vehicle (id, enabled, plate, brand, model, size, parker_id)
VALUES (2, true, 'XYZ5678', 'Honda', 'Civic', 'L', 1);

INSERT INTO Vehicle (id, enabled, plate, brand, model, size, parker_id)
VALUES (3, true, 'DEF9876', 'BMW', 'X5', 'XL', 1);

INSERT INTO Vehicle (id, enabled, plate, brand, model, size, parker_id)
VALUES (4, true, 'PAULA', 'Toyota', 'Corolla', 'M', 2);

INSERT INTO Vehicle (id, enabled, plate, brand, model, size, parker_id)
VALUES (5, true, 'ARTURO', 'Honda', 'Civic', 'L', 2);

INSERT INTO Vehicle (id, enabled, plate, brand, model, size, parker_id)
VALUES (6, true, 'DEF9876', 'BMW', 'X5', 'XL', 2);

INSERT INTO RESERVE (id, state, start_date, end_date, start_time, end_time, price, comments, parker_id, spot_id, vehicle_id)
VALUES (1, 'CONFIRMED', '2025-03-06', '2025-03-07', '10:00', '12:00', 5.0, 'Reserva para evento', 2, 1, 1);

INSERT INTO RESERVE (id, state, start_date, end_date, start_time, end_time, price, comments, parker_id, spot_id, vehicle_id)
VALUES (2, 'CONFIRMED', '2025-03-06', '2025-03-07', '10:00', '12:00', 8.0, 'Reserva para evento', 2, 2, 2);

INSERT INTO REQUEST (id, enabled, name, address, cp, city, country, telephone, email, feePerHour, openingTime, closingTime, longitude, latitude, idParking, totalSpots, state, enterprise_id)
VALUES (1, true, 'InterParking El Mercado', 'Calle Cerrajeros s/n', 28801, 'Alcalá de Henares', 'Espanya', '918798072', 'interparking@gmial.com', 1.1, '00:00', '00:00', 40.48205, -3.36553, null, 20, 'ANYADIR', 3);
*/
