DROP DATABASE IF EXISTS iLib;
CREATE DATABASE iLib;

USE iLib;

CREATE TABLE Voucher(
                        email VARCHAR(50) UNIQUE NOT NULL,
                        discount_percentage INT,
                        end_discount_date DATETIME,
                        PRIMARY KEY (email)
);

CREATE TABLE User (
                      email VARCHAR(50) UNIQUE NOT NULL,
                      phoneNumber CHAR(10),
                      fullName VARCHAR(50),
                      identityNumber CHAR(12) UNIQUE,
                      password VARCHAR(50),
                      PRIMARY KEY (email)
);

CREATE TABLE Books (
                       bookID VARCHAR(20),
                       bookName VARCHAR(50),
                       bookPrice INTEGER,
                       status VARCHAR(10),
                       genre VARCHAR(10),
                       PRIMARY KEY (bookID)
);

CREATE TABLE Category (
                          publisher VARCHAR(20),
                          genre VARCHAR(10),
                          publishYear YEAR,
                          email VARCHAR(50),
                          PRIMARY KEY (genre)
);

CREATE TABLE Rating (
                        starRating tinyint,
                        email VARCHAR(50),
                        bookID VARCHAR(20),
                        PRIMARY KEY (email, bookID)
);

CREATE TABLE Comment (
                         email VARCHAR(50),
                         bookID VARCHAR(20),
                         comment TEXT,
                         PRIMARY KEY (email, bookID)
);

CREATE TABLE Borrow (
                        bookID VARCHAR(20),
                        email VARCHAR(50),
                        borrowDate DATETIME,
                        borrowQuantity INT,
                        PRIMARY KEY (bookID, email)
);

CREATE TABLE Buy (
                     bookID VARCHAR(20),
                     email VARCHAR(50),
                     buyDate DATETIME,
                     buyQuantity INT,
                     PRIMARY KEY (bookID, email)
);

ALTER TABLE User ADD CONSTRAINT User_Voucher_fk FOREIGN KEY (email) REFERENCES Voucher (email);
ALTER TABLE Books ADD CONSTRAINT Books_Category_fk FOREIGN KEY (genre) REFERENCES Category (genre);
ALTER TABLE Category ADD CONSTRAINT Category_User_fk FOREIGN KEY (email) REFERENCES User (email);
ALTER TABLE Rating ADD CONSTRAINT Rating_User_fk FOREIGN KEY (email) REFERENCES User (email);
ALTER TABLE Rating ADD CONSTRAINT Rating_Books_fk FOREIGN KEY (bookID) REFERENCES Books (bookID);
ALTER TABLE Comment ADD CONSTRAINT Comment_User_fk FOREIGN KEY (email) REFERENCES User (email);
ALTER TABLE Comment ADD CONSTRAINT Comment_Books_fk FOREIGN KEY (bookID) REFERENCES Books (bookID);
ALTER TABLE Borrow ADD CONSTRAINT Borrow_User_fk FOREIGN KEY (Email) REFERENCES User (Email);
ALTER TABLE Borrow ADD CONSTRAINT Borrow_Books_fk FOREIGN KEY (bookID) REFERENCES Books (bookID);
ALTER TABLE Buy ADD CONSTRAINT Buy_User_fk FOREIGN KEY (Email) REFERENCES User (Email);
ALTER TABLE Buy ADD CONSTRAINT Buy_Books_fk FOREIGN KEY (bookID) REFERENCES Books (bookID);

INSERT INTO Voucher VALUES ('23021520@vnu.edu.vn', '100',
                            '2024/11/02');
INSERT INTO User VALUES ('23021520@vnu.edu.vn', '0779985683', 'Dat',
                         '000000000000', '12345678');

SELECT * FROM user;