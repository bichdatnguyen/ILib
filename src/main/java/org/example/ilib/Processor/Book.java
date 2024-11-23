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

        public Book(String image, String title, String author, String description, String id) {
                this.image = image;
                this.title = title;
                this.author = author;
                this.description = description;
                this.id = id;
        }

        public Book(String title, String image, String author) {
                this.title = title;
                this.image = image;
                this.author = author;
        }

        public int getQuantity() throws SQLException {
                DBConnection db = DBConnection.getInstance();
                return db.getQuantity(this.id);
        }

        public void setTitle(String title) {
                this.title = title;
        }

        public String getTitle() {
                return title;
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

        public String getId() {
                return id;
        }

        public String getDescription() {
                return description;
        }
}
