package org.example.ilib.Processor;
import javafx.scene.image.Image;

public class Book {
        private String title;
        private String image;
        private String author;


        public Book(String title, String image, String author) {
                this.title = title;
                this.image = image;
                this.author = author;
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
}
