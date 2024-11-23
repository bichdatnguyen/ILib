DROP DATABASE IF EXISTS ilib;

CREATE DATABASE IF NOT EXISTS ilib;

-- MySQL Workbench Forward Engineering

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
DROP DATABASE IF EXISTS ilib;
CREATE SCHEMA IF NOT EXISTS `ilib` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ilib` ;

-- -----------------------------------------------------
-- Table `ilib`.`books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`books` (
                                              `bookID` VARCHAR(50) NOT NULL,
    `title` TEXT NULL DEFAULT NULL,
    `bookPrice` INT NULL DEFAULT NULL,
    `quantityInStock` INT NULL DEFAULT NULL,
    PRIMARY KEY (`bookID`),
    UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE)
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
    PRIMARY KEY (`Email`),
    UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`payment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`payment` (
                                                `bookID` VARCHAR(50) NOT NULL,
    `email` VARCHAR(50) NOT NULL,
    `date` DATE NULL DEFAULT NULL,
    `quantity` INT NULL DEFAULT NULL,
    `type` VARCHAR(10) NULL,
    UNIQUE INDEX `Email_UNIQUE` (`bookID` ASC) VISIBLE,
    INDEX `fk_Buy_User_idx` (`email` ASC) VISIBLE,
    CONSTRAINT `fk_Payment_Books`
    FOREIGN KEY (`bookID`)
    REFERENCES `ilib`.`books` (`bookID`)
    ON UPDATE CASCADE,
    CONSTRAINT `fk_Payment_User`
    FOREIGN KEY (`email`)
    REFERENCES `ilib`.`user` (`Email`)
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ilib`.`cart`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`cart` (
                                             `bookID` VARCHAR(20) NOT NULL,
    `email` VARCHAR(50) NOT NULL,
    `date` DATE ,
    `quantity` INT NOT NULL,
    `type` VARCHAR(10) NULL DEFAULT NULL,
    PRIMARY KEY (`bookID`, `email`),
    INDEX `fk_cart_User` (`email` ASC) VISIBLE,
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
                                                `Email` VARCHAR(50) NOT NULL,
    `bookID` VARCHAR(50) NOT NULL,
    `Time` DATETIME NOT NULL,
    PRIMARY KEY (`Email`, `bookID`),
    UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE,
    UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE,
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
-- Table `ilib`.`rating`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ilib`.`rating` (
                                               `Email` VARCHAR(50) NOT NULL,
    `bookID` VARCHAR(50) NOT NULL,
    `Comment` TEXT NULL DEFAULT NULL,
    `Time` DATETIME NULL DEFAULT NULL,
    PRIMARY KEY (`Email`, `bookID`),
    UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE,
    UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE,
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
select * from books;
insert into user values ('23021524@vnu.edu.vn','1234567890','Vu Dat','123456');
insert into books values ('12','Name',12,12);
select * from cart;
delete from cart where email = '23021524@vnu.edu.vn' and bookID = '12';
select * from books;
select * from payment;