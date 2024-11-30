module org.example.ilib {
    requires javafx.web;
    requires org.controlsfx.controls;


    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.auth;
    requires google.api.services.drive.v3.rev136;
    requires google.oauth.client.java6;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.gson;

//    opens org.example.ilib to javafx.fxml;
//
//    exports org.example.ilib.Controller;
//    opens org.example.ilib.Controller to javafx.fxml;
//    exports org.example.ilib.Processor;
//    opens org.example.ilib.Processor to javafx.fxml;
//



    requires com.google.zxing;
    requires com.google.zxing.javase;

    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires spring.webmvc;  // Nếu sử dụng Spring MVC
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

    exports org.example.ilib.Controller;
    opens org.example.ilib.Controller to javafx.fxml;
    exports org.example.ilib.Processor;
    opens org.example.ilib.Processor to javafx.fxml;
}