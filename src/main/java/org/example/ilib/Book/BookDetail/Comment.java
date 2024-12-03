package org.example.ilib.Book.BookDetail;

import java.sql.Timestamp;

public class Comment {
        private String email;
        private String comment;
        private String bookID;
        private Timestamp date;

        public Comment(String email, String comment, Timestamp date) {
            this.email = email;
            this.comment = comment;
            this.date = date;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getComment() {
            return comment;
        }


        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getBookID() {
            return bookID;
        }

        public void setBookID(String bookID) {
            this.bookID = bookID;
        }

        public Timestamp getDate() {
            return date;
        }

        public void setDate(Timestamp date) {
            this.date = date;
        }
}
