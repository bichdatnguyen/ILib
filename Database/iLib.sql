-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ILIB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ILIB
-- -----------------------------------------------------

Drop database if exists ILIB;
CREATE SCHEMA IF NOT EXISTS `ILIB` DEFAULT CHARACTER SET utf8 ;
USE `ILIB` ;

-- -----------------------------------------------------
-- Table `ILIB`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ILIB`.`User` (
`Email` VARCHAR(50) NOT NULL,
`phoneNumber` CHAR(10) NULL,
`fullName` VARCHAR(50) NULL,
`password` VARCHAR(50) NULL,
PRIMARY KEY (`Email`),
UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ILIB`.`Voucher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ILIB`.`Voucher` (
`Email` VARCHAR(50) NOT NULL,
`discountPercentage` INT NULL,
PRIMARY KEY (`Email`),
UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE,
CONSTRAINT `fk_Voucher_User`
    FOREIGN KEY (`Email`)
        REFERENCES `ILIB`.`User` (`Email`)
        ON DELETE NO ACTION
        ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ILIB`.`Books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ILIB`.`Books` (
`bookID` VARCHAR(50) NOT NULL,
`title` VARCHAR(50) NULL,
`authors` VARCHAR(50) NULL,
`bookPrice` INT NULL,
`Description` TEXT NULL,
`quantityInStock` INT NULL,
PRIMARY KEY (`bookID`),
UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ILIB`.`Categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ILIB`.`Categories` (
`Category` VARCHAR(50) NOT NULL,
`bookID` VARCHAR(50) NOT NULL,
PRIMARY KEY (`Category`, `bookID`),
UNIQUE INDEX `Category_UNIQUE` (`Category` ASC) VISIBLE,
UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE,
CONSTRAINT `fk_Categories_Books`
   FOREIGN KEY (`bookID`)
       REFERENCES `ILIB`.`Books` (`bookID`)
       ON DELETE NO ACTION
       ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ILIB`.`Rating`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ILIB`.`Rating` (
`Email` VARCHAR(50) NOT NULL,
`bookID` VARCHAR(50) NOT NULL,
`averageRating` TINYINT NULL,
`Comment` TEXT NULL,
`Time` DATETIME NULL,
PRIMARY KEY (`Email`, `bookID`),
UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE,
UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE,
CONSTRAINT `fk_Rating_User`
   FOREIGN KEY (`Email`)
       REFERENCES `ILIB`.`User` (`Email`)
       ON DELETE NO ACTION
       ON UPDATE CASCADE,
CONSTRAINT `fk_Rating_Books`
   FOREIGN KEY (`bookID`)
       REFERENCES `ILIB`.`Books` (`bookID`)
       ON DELETE NO ACTION
       ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ILIB`.`Borrow`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ILIB`.`Borrow` (
`bookID` VARCHAR(20) NOT NULL,
`email` VARCHAR(50) NOT NULL,
`borrowDate` DATETIME NULL,
`quantity` INT NULL,
PRIMARY KEY (`bookID`, `email`),
UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE,
UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
CONSTRAINT `fk_Borrow_Books`
   FOREIGN KEY (`bookID`)
       REFERENCES `ILIB`.`Books` (`bookID`)
       ON DELETE NO ACTION
       ON UPDATE CASCADE,
CONSTRAINT `fk_Borrow_User`
   FOREIGN KEY (`email`)
       REFERENCES `ILIB`.`User` (`Email`)
       ON DELETE NO ACTION
       ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ILIB`.`Buy`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ILIB`.`Buy` (
`Email` VARCHAR(50) NOT NULL,
`bookID` VARCHAR(50) NOT NULL,
`buyDate` DATETIME NULL,
`quantity` INT NULL,
PRIMARY KEY (`Email`, `bookID`),
UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE,
UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE,
CONSTRAINT `fk_Buy_Books`
    FOREIGN KEY (`bookID`)
        REFERENCES `ILIB`.`Books` (`bookID`)
        ON DELETE NO ACTION
        ON UPDATE CASCADE,
CONSTRAINT `fk_Buy_User`
    FOREIGN KEY (`Email`)
        REFERENCES `ILIB`.`User` (`Email`)
        ON DELETE NO ACTION
        ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ILIB`.`History`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ILIB`.`History` (
`Email` VARCHAR(50) NOT NULL,
`bookID` VARCHAR(50) NOT NULL,
`Time` DATETIME NOT NULL,
PRIMARY KEY (`Email`, `bookID`),
UNIQUE INDEX `Email_UNIQUE` (`Email` ASC) VISIBLE,
UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE,
CONSTRAINT `fk_History_User`
    FOREIGN KEY (`Email`)
        REFERENCES `ILIB`.`User` (`Email`)
        ON DELETE NO ACTION
        ON UPDATE CASCADE,
CONSTRAINT `fk_History_Books`
    FOREIGN KEY (`bookID`)
        REFERENCES `ILIB`.`Books` (`bookID`)
        ON DELETE NO ACTION
        ON UPDATE CASCADE)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ILIB`.`Author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ILIB`.`Author` (
`bookID` VARCHAR(50) NOT NULL,
`authorName` VARCHAR(50) NOT NULL,
PRIMARY KEY (`bookID`, `authorName`),
UNIQUE INDEX `bookID_UNIQUE` (`bookID` ASC) VISIBLE,
UNIQUE INDEX `authorName_UNIQUE` (`authorName` ASC) VISIBLE,
CONSTRAINT `fk_Author_Books`
   FOREIGN KEY (`bookID`)
       REFERENCES `ILIB`.`Books` (`bookID`)
       ON DELETE NO ACTION
       ON UPDATE CASCADE)
    ENGINE = InnoDB;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
insert into User values ('23021524@vnu.edu.vn','0914435450','Vu Dat','123456');
select * from User;