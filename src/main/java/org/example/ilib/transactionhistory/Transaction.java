package org.example.ilib.transactionhistory;

import com.google.api.client.util.DateTime;

public class Transaction {
    private String paymentID;
    private String bookID;
    private String email;
    private DateTime date;
    private int quantity;
    private String type;
    private int priceEach;
    private String title;

    public Transaction(String paymentID, String bookID, String email, DateTime date, int quantity, String type, String title, int priceEach) {
        this.paymentID = paymentID;
        this.bookID = bookID;
        this.email = email;
        this.date = date;
        this.quantity = quantity;
        this.type = type;
        this.title = title;
        this.priceEach = priceEach;
    }

    public Transaction(){

    }

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public int getPriceEach() {
        return priceEach;
    }
    public void setPriceEach(int priceEach) {
        this.priceEach = priceEach;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
