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
//    exports org.example.ilib.Controller;
//    opens org.example.ilib.Controller to javafx.fxml;
//    exports org.example.ilib.Processor;
//    opens org.example.ilib.Processor to javafx.fxml;
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

    exports org.example.ilib.Controller;
    opens org.example.ilib.Controller to javafx.fxml;
    exports org.example.ilib.Processor;
    opens org.example.ilib.Processor to javafx.fxml;
    exports org.example.ilib.Account;
    opens org.example.ilib.Account to javafx.fxml;
    exports org.example.ilib.Book;
    opens org.example.ilib.Book to javafx.fxml;
    exports org.example.ilib.LoginAndRegister;
    opens org.example.ilib.LoginAndRegister to javafx.fxml;
    exports org.example.ilib.Menu;
    opens org.example.ilib.Menu to javafx.fxml;
    exports org.example.ilib.BookList;
    opens org.example.ilib.BookList to javafx.fxml;
    exports org.example.ilib.AdminMenu.BookAdmin;
    opens org.example.ilib.AdminMenu.BookAdmin to javafx.fxml;
    exports org.example.ilib.AdminMenu;
    opens org.example.ilib.AdminMenu to javafx.fxml;
    exports org.example.ilib.Cart;
    opens org.example.ilib.Cart to javafx.fxml;
    exports org.example.ilib.AdminMenu.Member;
    opens org.example.ilib.AdminMenu.Member to javafx.fxml;
    exports org.example.ilib.TransactionHistory;
    opens org.example.ilib.TransactionHistory to javafx.fxml;
    exports org.example.ilib.Book.BookDetail;
    opens org.example.ilib.Book.BookDetail to javafx.fxml;
}