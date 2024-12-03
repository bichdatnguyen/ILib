package org.example.ilib.AdminMenu.Member;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {
    private StringProperty email;
    private StringProperty name;
    private StringProperty phone;
    private StringProperty password;

    public Member(String email, String name, String phone, String password) {
        this.email = new SimpleStringProperty(email);
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.password = new SimpleStringProperty(password);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public StringProperty passwordProperty() {
        return password;
    }


}
