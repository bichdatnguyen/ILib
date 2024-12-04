DROP DATABASE IF EXISTS ilib;

CREATE DATABASE IF NOT EXISTS ilib;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `ilib` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `ilib`;

-- Table `books`
CREATE TABLE IF NOT EXISTS `ilib`.`books` (
      `bookID` VARCHAR(50) NOT NULL,
      `thumbnail` VARCHAR(500) NULL,
      `description` TEXT NULL,
      `title` TEXT NULL DEFAULT NULL,
      `bookPrice` INT NULL DEFAULT NULL,
      `averageRating` DOUBLE NULL DEFAULT NULL,
      `quantityInStock` INT NULL DEFAULT NULL,
      `addDate` DATETIME NULL DEFAULT NULL,
      PRIMARY KEY (`bookID`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `author`
CREATE TABLE IF NOT EXISTS `ilib`.`author` (
   `bookID` VARCHAR(50) NOT NULL,
   `authorName` VARCHAR(50) NOT NULL,
   PRIMARY KEY (`bookID`, `authorName`),
   CONSTRAINT `fk_Author_Books`
       FOREIGN KEY (`bookID`)
           REFERENCES `ilib`.`books` (`bookID`)
           ON DELETE CASCADE
           ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `user`
CREATE TABLE IF NOT EXISTS `ilib`.`user` (
         `Email` VARCHAR(50) NOT NULL,
         `phoneNumber` CHAR(10) NULL DEFAULT NULL,
         `fullName` VARCHAR(50) NULL DEFAULT NULL,
         `password` VARCHAR(50) NULL DEFAULT NULL,
         `avatarPath` VARCHAR(255) NULL DEFAULT NULL,
         `role` VARCHAR(10) NOT NULL,
         PRIMARY KEY (`Email`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `borrow`
CREATE TABLE IF NOT EXISTS `ilib`.`borrow` (
        `Email` VARCHAR(50) NOT NULL,
        `bookID` VARCHAR(50) NOT NULL,
        `borrowDate` DATETIME NULL DEFAULT NULL,
        `returnDate` DATETIME NULL DEFAULT NULL,
        PRIMARY KEY (`Email`, `bookID`),
        CONSTRAINT `fk_borrow_books`
        FOREIGN KEY (`bookID`)
           REFERENCES `ilib`.`books` (`bookID`)
           ON DELETE CASCADE
           ON UPDATE CASCADE,
        CONSTRAINT `fk_borrow_user`
        FOREIGN KEY (`Email`)
           REFERENCES `ilib`.`user` (`Email`)
           ON DELETE CASCADE
           ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `cart`
CREATE TABLE IF NOT EXISTS `ilib`.`cart` (
         `email` VARCHAR(50) NOT NULL,
         `bookID` VARCHAR(20) NOT NULL,
         `date` DATE NULL DEFAULT NULL,
         `quantity` INT NOT NULL,
         `type` VARCHAR(10) NULL DEFAULT NULL,
         PRIMARY KEY (`email`, `bookID`),
         CONSTRAINT `fk_cart_Books`
             FOREIGN KEY (`bookID`)
                 REFERENCES `ilib`.`books` (`bookID`)
                 ON DELETE CASCADE
                 ON UPDATE CASCADE,
         CONSTRAINT `fk_cart_User`
             FOREIGN KEY (`email`)
                 REFERENCES `ilib`.`user` (`Email`)
                 ON DELETE CASCADE
                 ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `categories`
CREATE TABLE IF NOT EXISTS `ilib`.`categories` (
       `Category` VARCHAR(50) NOT NULL,
       `bookID` VARCHAR(50) NOT NULL,
       PRIMARY KEY (`Category`, `bookID`),
       CONSTRAINT `fk_Categories_Books`
           FOREIGN KEY (`bookID`)
               REFERENCES `ilib`.`books` (`bookID`)
               ON DELETE CASCADE
               ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `history`
CREATE TABLE IF NOT EXISTS `ilib`.`history` (
    `historyID` INT NOT NULL AUTO_INCREMENT,
    `Email` VARCHAR(50) NOT NULL,
    `bookID` VARCHAR(50) NOT NULL,
    `Time` DATETIME NOT NULL,
    PRIMARY KEY (`historyID`),
    CONSTRAINT `fk_History_Books`
        FOREIGN KEY (`bookID`)
            REFERENCES `ilib`.`books` (`bookID`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_History_User`
        FOREIGN KEY (`Email`)
            REFERENCES `ilib`.`user` (`Email`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `payment`
CREATE TABLE IF NOT EXISTS `ilib`.`payment` (
`paymentID` INT NOT NULL AUTO_INCREMENT,
`bookID` VARCHAR(50) NOT NULL,
`email` VARCHAR(50) NOT NULL,
`date` DATETIME NULL DEFAULT NULL,
`quantity` INT NULL DEFAULT NULL,
`type` VARCHAR(10) NULL DEFAULT NULL,
PRIMARY KEY (`paymentID`),
CONSTRAINT `fk_Payment_Books`
    FOREIGN KEY (`bookID`)
        REFERENCES `ilib`.`books` (`bookID`)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
CONSTRAINT `fk_Payment_User`
    FOREIGN KEY (`email`)
        REFERENCES `ilib`.`user` (`Email`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `rating`
CREATE TABLE IF NOT EXISTS `ilib`.`rating` (
`Email` VARCHAR(50) NOT NULL,
`bookID` VARCHAR(50) NOT NULL,
`Comment` TEXT NULL DEFAULT NULL,
`Time` DATETIME NOT NULL,
PRIMARY KEY (`Email`, `bookID`, `Time`),
CONSTRAINT `fk_Rating_Books`
   FOREIGN KEY (`bookID`)
       REFERENCES `ilib`.`books` (`bookID`)
       ON DELETE CASCADE
       ON UPDATE CASCADE,
CONSTRAINT `fk_Rating_User`
   FOREIGN KEY (`Email`)
       REFERENCES `ilib`.`user` (`Email`)
       ON DELETE CASCADE
       ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `shelf`
CREATE TABLE IF NOT EXISTS `ilib`.`shelf` (
  `Email` VARCHAR(50) NOT NULL,
  `bookID` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`Email`, `bookID`),
  CONSTRAINT `fk_shelf_books`
      FOREIGN KEY (`bookID`)
          REFERENCES `ilib`.`books` (`bookID`)
          ON DELETE CASCADE
          ON UPDATE CASCADE,
  CONSTRAINT `fk_shelf_user`
      FOREIGN KEY (`Email`)
          REFERENCES `ilib`.`user` (`Email`)
          ON DELETE CASCADE
          ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

-- Table `voucher`
CREATE TABLE IF NOT EXISTS `ilib`.`voucher` (
    `Email` VARCHAR(50) NOT NULL,
    `discountPercentage` INT NULL DEFAULT NULL,
    PRIMARY KEY (`Email`),
    CONSTRAINT `fk_Voucher_User`
        FOREIGN KEY (`Email`)
            REFERENCES `ilib`.`user` (`Email`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8mb3;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

# SHOW VARIABLES LIKE 'max_connections';
SET GLOBAL max_connections = 500;

select * from user;
select * from books;
delete from books where bookID ='XdmImQEACAAJ';
select * from author;
select * from cart;
insert into user values ('ton@gmail.com','1234567890','Vu Dat','1234556',null,'user');
select * from payment;
select * from borrow;
insert into user values ('ton1@gmail.com','1234567890','Vu Dat','1234556',null,'admin');
insert into user values ('vudat090305@gmail.com','1234567890','Vu Dat','123456',null,'user');
INSERT INTO user VALUES ('bichdat05@gmail.com', '0779985683', 'Nguyễn Bích Đạt', '88888888', null, 'admin');