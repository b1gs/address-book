DROP DATABASE IF EXISTS ADDRESS_BOOK_DEV;
DROP DATABASE IF EXISTS ADDRESS_BOOK_TEST;

CREATE DATABASE ADDRESS_BOOK_DEV;
CREATE DATABASE ADDRESS_BOOK_TEST;

CREATE USER IF NOT EXISTS 'address-book'@'localhost' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON ADDRESS_BOOK_DEV.* TO 'address-book' @'localhost';
GRANT ALL PRIVILEGES ON ADDRESS_BOOK_TEST.* TO 'address-book' @'localhost';