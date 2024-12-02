DROP DATABASE IF EXISTS ilib;

CREATE DATABASE IF NOT EXISTS ilib;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ilib
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ilib
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ilib` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ilib` ;

-- -----------------------------------------------------
-- Table `ilib`.`books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`books` (
      `bookID` VARCHAR(50) NOT NULL,
      `thumbnail` VARCHAR(500) NULL,
      `description` TEXT NULL,
      `title` TEXT NULL DEFAULT NULL,
      `bookPrice` INT NULL DEFAULT NULL,
      `averageRating` DOUBLE NULL DEFAULT NULL,
      `quantityInStock` INT NULL DEFAULT NULL,
      `addDate` DATETIME NULL DEFAULT NULL,
      PRIMARY KEY (`bookID`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`author` (
       `bookID` VARCHAR(50) NOT NULL,
       `authorName` VARCHAR(50) NOT NULL,
       PRIMARY KEY (`bookID`, `authorName`),
       CONSTRAINT `fk_Author_Books`
           FOREIGN KEY (`bookID`)
               REFERENCES `ilib`.`books` (`bookID`)
               ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`user` (
         `Email` VARCHAR(50) NOT NULL,
         `phoneNumber` CHAR(10) NULL DEFAULT NULL,
         `fullName` VARCHAR(50) NULL DEFAULT NULL,
         `password` VARCHAR(50) NULL DEFAULT NULL,
         `avatarPath` VARCHAR(255) NULL DEFAULT NULL,
         `role` VARCHAR(10) NOT NULL,
         PRIMARY KEY (`Email`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`borrow`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`borrow` (
       `Email` VARCHAR(50) NOT NULL,
       `bookID` VARCHAR(50) NOT NULL,
       `borrowDate` DATETIME NULL DEFAULT NULL,
       `returnDate` DATETIME NULL DEFAULT NULL,
       PRIMARY KEY (`Email`, `bookID`),
       INDEX `fk_borrow_books_idx` (`bookID` ASC) VISIBLE,
       CONSTRAINT `fk_borrow_books`
           FOREIGN KEY (`bookID`)
               REFERENCES `ilib`.`books` (`bookID`)
               ON DELETE CASCADE
               ON UPDATE CASCADE,
       CONSTRAINT `fk_borrow_user`
           FOREIGN KEY (`Email`)
               REFERENCES `ilib`.`user` (`Email`)
               ON DELETE CASCADE
               ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`cart` (
     `email` VARCHAR(50) NOT NULL,
     `bookID` VARCHAR(20) NOT NULL,
     `date` DATE NULL DEFAULT NULL,
     `quantity` INT NOT NULL,
     `type` VARCHAR(10) NULL DEFAULT NULL,
     PRIMARY KEY (`email`, `bookID`),
     INDEX `fk_cart_User` (`email` ASC) VISIBLE,
     INDEX `fk_cart_Books` (`bookID` ASC) VISIBLE,
     CONSTRAINT `fk_cart_Books`
         FOREIGN KEY (`bookID`)
             REFERENCES `ilib`.`books` (`bookID`)
             ON UPDATE CASCADE,
     CONSTRAINT `fk_cart_User`
         FOREIGN KEY (`email`)
             REFERENCES `ilib`.`user` (`Email`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`categories` (
   `Category` VARCHAR(50) NOT NULL,
   `bookID` VARCHAR(50) NOT NULL,
   PRIMARY KEY (`Category`, `bookID`),
   INDEX `fk_Categories_Books` (`bookID` ASC) VISIBLE,
   CONSTRAINT `fk_Categories_Books`
       FOREIGN KEY (`bookID`)
           REFERENCES `ilib`.`books` (`bookID`)
           ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`history`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`history` (
    `historyID` INT NOT NULL AUTO_INCREMENT,
    `Email` VARCHAR(50) NOT NULL,
    `bookID` VARCHAR(50) NOT NULL,
    `Time` DATETIME NOT NULL,
    PRIMARY KEY (`historyID`),
    INDEX `fk_History_Books` (`bookID` ASC) VISIBLE,
    INDEX `fk_History_User` (`Email` ASC) VISIBLE,
    CONSTRAINT `fk_History_Books`
        FOREIGN KEY (`bookID`)
            REFERENCES `ilib`.`books` (`bookID`)
            ON UPDATE CASCADE,
    CONSTRAINT `fk_History_User`
        FOREIGN KEY (`Email`)
            REFERENCES `ilib`.`user` (`Email`)
            ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`payment`
-- -----------------------------------------------------
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`payment` (
`paymentID` INT NOT NULL AUTO_INCREMENT,
`bookID` VARCHAR(50) NOT NULL,
`email` VARCHAR(50) NOT NULL,
`date` DATETIME NULL DEFAULT NULL,
`quantity` INT NULL DEFAULT NULL,
`type` VARCHAR(10) NULL DEFAULT NULL,
PRIMARY KEY (`paymentID`),
INDEX `fk_Buy_User_idx` (`email` ASC) VISIBLE,
CONSTRAINT `fk_Payment_Books`
    FOREIGN KEY (`bookID`)
        REFERENCES `ilib`.`books` (`bookID`)
        ON UPDATE CASCADE,
CONSTRAINT `fk_Payment_User`
    FOREIGN KEY (`email`)
        REFERENCES `ilib`.`user` (`Email`)
        ON UPDATE CASCADE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;



-- -----------------------------------------------------
-- Table `ilib`.`rating`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`rating` (
   `Email` VARCHAR(50) NOT NULL,
   `bookID` VARCHAR(50) NOT NULL,
   `Comment` TEXT NULL DEFAULT NULL,
   `Time` DATETIME NOT NULL,
   PRIMARY KEY (`Email`, `bookID`, `Time`),
   INDEX `fk_Rating_Books` (`bookID` ASC) VISIBLE,
   CONSTRAINT `fk_Rating_Books`
       FOREIGN KEY (`bookID`)
           REFERENCES `ilib`.`books` (`bookID`)
           ON UPDATE CASCADE,
   CONSTRAINT `fk_Rating_User`
       FOREIGN KEY (`Email`)
           REFERENCES `ilib`.`user` (`Email`)
           ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`shelf`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`shelf` (
      `Email` VARCHAR(50) NOT NULL,
      `bookID` VARCHAR(50) NOT NULL,
      PRIMARY KEY (`Email`, `bookID`),
      INDEX `fk_shelf_books_idx` (`bookID` ASC) VISIBLE,
      CONSTRAINT `fk_shelf_books`
          FOREIGN KEY (`bookID`)
              REFERENCES `ilib`.`books` (`bookID`)
              ON DELETE CASCADE
              ON UPDATE CASCADE,
      CONSTRAINT `fk_shelf_user`
          FOREIGN KEY (`Email`)
              REFERENCES `ilib`.`user` (`Email`)
              ON DELETE CASCADE
              ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`voucher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`voucher` (
    `Email` VARCHAR(50) NOT NULL,
    `discountPercentage` INT NULL DEFAULT NULL,
    PRIMARY KEY (`Email`),
    UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE,
    CONSTRAINT `fk_Voucher_User`
        FOREIGN KEY (`Email`)
            REFERENCES `ilib`.`user` (`Email`)
            ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
# SHOW VARIABLES LIKE 'max_connections';
SET GLOBAL max_connections = 500;

select * from user;
select * from books;
select * from author;
select * from cart;
insert into user values ('ton@gmail.com','1234567890','Vu Dat','1234556',null,'user');
select * from payment;
select * from borrow;
insert into user values ('ton1@gmail.com','1234567890','Vu Dat','1234556',null,'admin');
insert into user values ('vudat090305@gmail.com','1234567890','Vu Dat','123456',null,'user');
INSERT INTO user VALUES ('bichdat05@gmail.com', '0779985683', 'Nguyễn Bích Đạt', '88888888', null, 'admin');