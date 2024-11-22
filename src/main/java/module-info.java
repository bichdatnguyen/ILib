module org.example.ilib {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;


    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.auth;
    requires google.api.services.drive.v3.rev136;
    requires google.oauth.client.java6;
    requires com.google.api.client.extensions.jetty.auth;
    requires com.google.gson;
    requires java.desktop;

//    opens org.example.ilib to javafx.fxml;
//
//    exports org.example.ilib.Controller;
//    opens org.example.ilib.Controller to javafx.fxml;
//    exports org.example.ilib.Processor;
//    opens org.example.ilib.Processor to javafx.fxml;




    requires com.google.zxing;
    requires com.google.zxing.javase;

    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires spring.webmvc;  // Nếu sử dụng Spring MVC
    requires stripe.java;

    requires java.logging;
    opens org.example.ilib to javafx.fxml;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires mysql.connector.j;
    requires org.json;


    requires javafx.swing;
    requires spark.core;
    requires java.base;
    requires org.eclipse.jetty.util;


    exports org.example.ilib.Controller;
    opens org.example.ilib.Controller to javafx.fxml;
    exports org.example.ilib.Processor;
    opens org.example.ilib.Processor to javafx.fxml;







}