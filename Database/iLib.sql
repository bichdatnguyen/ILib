DROP DATABASE IF EXISTS iLib;
CREATE DATABASE iLib;

USE iLib;
-- Tạo bảng User trước
CREATE TABLE User (
                      email VARCHAR(50) UNIQUE NOT NULL,
                      phoneNumber CHAR(100),
                      fullName VARCHAR(500),
                      identityNumber CHAR(120) UNIQUE,
                      password VARCHAR(50),
                      PRIMARY KEY (email)
);

-- Tạo bảng Voucher sau
CREATE TABLE Voucher (
                         email VARCHAR(50) PRIMARY KEY UNIQUE NOT NULL,
                         discount_percentage INT,
                         end_discount_date DATE,
                         CONSTRAINT User_Voucher_fk FOREIGN KEY (email) REFERENCES User(email) ON UPDATE CASCADE
);

CREATE TABLE Category (
                          publisher VARCHAR(20),
                          genre VARCHAR(10),
                          publishYear YEAR,
                          email VARCHAR(50),
                          PRIMARY KEY (genre)

);

CREATE TABLE Books (
                       bookID VARCHAR(20),
                       bookName VARCHAR(50),
                       bookPrice INTEGER,
                       status VARCHAR(10),
                       genre VARCHAR(10),
                       PRIMARY KEY (bookID),
                       constraint Books_Category_fk foreign key (genre) references Category(genre) on update  cascade
);

CREATE TABLE Rating (
                        starRating tinyint,
                        email VARCHAR(50),
                        bookID VARCHAR(20),
                        PRIMARY KEY (email, bookID),
                        CONSTRAINT Rating_User_fk FOREIGN KEY (email) REFERENCES User (email) on update cascade ,
                        CONSTRAINT Rating_Books_fk FOREIGN KEY (bookID) REFERENCES Books (bookID) on update cascade

);

CREATE TABLE Comment (
                         email VARCHAR(50),
                         bookID VARCHAR(20),
                         comment TEXT,
                         PRIMARY KEY (email, bookID),
                         CONSTRAINT Comment_User_fk FOREIGN KEY (email) REFERENCES User (email) on update cascade ,
                         CONSTRAINT Comment_Books_fk FOREIGN KEY (bookID) REFERENCES Books (bookID) on update cascade

);

CREATE TABLE Borrow (
                        bookID VARCHAR(20),
                        email VARCHAR(50),
                        borrowDate DATETIME,
                        borrowQuantity INT,
                        PRIMARY KEY (bookID, email),
                        CONSTRAINT Borrow_User_fk FOREIGN KEY (Email) REFERENCES User (Email) on update cascade ,
                        CONSTRAINT Borrow_Books_fk FOREIGN KEY (bookID) REFERENCES Books (bookID) on update cascade
);

CREATE TABLE Buy (
                     bookID VARCHAR(20),
                     email VARCHAR(50),
                     buyDate DATETIME,
                     buyQuantity INT,
                     PRIMARY KEY (bookID, email),
                     CONSTRAINT Buy_User_fk FOREIGN KEY (Email) REFERENCES User (Email) on update cascade ,
                     CONSTRAINT Buy_Books_fk FOREIGN KEY (bookID) REFERENCES Books (bookID) on update cascade
);


INSERT INTO User VALUES ('23021520@vnu.edu.vn', '0779985683', 'Dat',
                         '000000000000', '12345678');
INSERT INTO Voucher VALUES ('23021520@vnu.edu.vn', '100',
                            '2024/11/02');
SELECT * FROM user;
