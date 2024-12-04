module org.example.ilib {
    requires javafx.web;
    requires org.controlsfx.controls;


    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.auth;
    requires google.api.services.drive.v3.rev136;
    requires com.google.gson;

//    opens org.example.ilib to javafx.fxml;
//
//    exports org.example.ilib.controller;
//    opens org.example.ilib.controller to javafx.fxml;
//    exports org.example.ilib.processor;
//    opens org.example.ilib.processor to javafx.fxml;
//

    requires stripe.java;

    opens org.example.ilib to javafx.fxml;
    requires ch.qos.logback.classic;
    requires mysql.connector.j;
    requires org.json;

    requires javafx.swing;
    requires spark.core;
    requires org.eclipse.jetty.util;
    requires org.xerial.sqlitejdbc;
    requires com.zaxxer.hikari;
    requires org.apache.logging.log4j;
    requires java.net.http;
    requires javafx.fxml;
    requires java.mail;
    requires javafx.controls;
    requires jdk.compiler;
    requires javafx.media;
    requires com.google.zxing;

    exports org.example.ilib.controller;
    opens org.example.ilib.controller to javafx.fxml;
    exports org.example.ilib.processor;
    opens org.example.ilib.processor to javafx.fxml;
    exports org.example.ilib.account;
    opens org.example.ilib.account to javafx.fxml;
    exports org.example.ilib.book;
    opens org.example.ilib.book to javafx.fxml;
    exports org.example.ilib.loginandregister;
    opens org.example.ilib.loginandregister to javafx.fxml;
    exports org.example.ilib.menu;
    opens org.example.ilib.menu to javafx.fxml;
    exports org.example.ilib.booklist;
    opens org.example.ilib.booklist to javafx.fxml;
    exports org.example.ilib.adminmenu.BookAdmin;
    opens org.example.ilib.adminmenu.BookAdmin to javafx.fxml;
    exports org.example.ilib.adminmenu;
    opens org.example.ilib.adminmenu to javafx.fxml;
    exports org.example.ilib.cart;
    opens org.example.ilib.cart to javafx.fxml;
    exports org.example.ilib.adminmenu.Member;
    opens org.example.ilib.adminmenu.Member to javafx.fxml;
    exports org.example.ilib.transactionhistory;
    opens org.example.ilib.transactionhistory to javafx.fxml;
    exports org.example.ilib.book.BookDetail;
    opens org.example.ilib.book.BookDetail to javafx.fxml;
}