package org.example.ilib.Processor;
import javafx.scene.image.Image;

public class Book {
        private String id;
        private String title;
        private String author;
        private int price;
        private String image;

        public Book(String title, String image, String author) {
                this.title = title;
                this.image = image;
                this.author = author;
        }

        public Book(String id, String title, String author, int price) {
                this.id = id;
                this.title = title;
                this.author = author;
                this.price = price;
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

        public String getId() {
                return id;
        }

        public int getPrice() {
                return price;
        }
}
