package org.example.ilib.Processor;
import org.example.ilib.Controller.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Book {
        private String id;
        private String title;
        private String author;
        private String image;
        private String description;
        private int quantity;
        private int price;
        public Book(){}
        public Book(String id) {
                this.id = id;
        }

        public Book(String image, String title, String author, String description, String id) {
                this.image = image;
                this.title = title;
                this.author = author;
                this.description = description;
                this.id = id;
        }
        public Book(String id, String title, String author, int quantity, int price){
                this.id = id;
                this.title = title;
                this.author = author;
                this.quantity = quantity;
                this.price = price;
        }

        public Book(String image, String title, String author, String description,
                    String id, int quantity) {
                this.image = image;
                this.title = title;
                this.author = author;
                this.description = description;
                this.id = id;
                this.quantity = quantity;
        }

        public Book(String title, String image, String author) {
                this.title = title;
                this.image = image;
                this.author = author;
        }

        public int getQuantity() {
                return quantity;
        }
        public void setQuantity(int quantity) {
                this.quantity = quantity;
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getTitle() {
                return title;
        }
        public void setAuthor(String author) {
                this.author = author;
        }

        public String getAuthor() {
                return author;
        }

        public void setImage(String image) {
                this.image = image;
        }

        public String getImage() {
                return image;
        }

        public void setId(String id) {
                this.id = id;
        }

        public String getId() {
                return id;
        }

        public String getDescription() {
                return description;
        }
        public void setDescription(String description) {
                this.description = description;
        }
        public int getPrice() {
                return price;
        }
        public void setPrice(int price) {
                this.price = price;
        }
}
