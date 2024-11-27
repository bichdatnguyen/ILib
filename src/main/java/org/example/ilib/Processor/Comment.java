package org.example.ilib.Processor;

import com.google.api.client.util.DateTime;

public class Comment {
        private String email;
        private String comment;
        private String thumbnail;
        private String name;
        private String bookID;
        private DateTime date;

        public Comment(String email, String comment, String thumbnail, String name,String bookID, DateTime date) {
            this.email = email;
            this.comment = comment;
            this.thumbnail = thumbnail;
            this.name = name;
            this.bookID = bookID;
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
        public String getThumbnail() {
            return thumbnail;
        }
        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getBookID() {
            return bookID;
        }
        public void setBookID(String bookID) {
            this.bookID = bookID;
        }
        public DateTime getDate() {
            return date;
        }
        public void setDate(DateTime date) {
            this.date = date;
        }
}
