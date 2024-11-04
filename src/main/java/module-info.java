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

    opens org.example.ilib to javafx.fxml;
    exports org.example.ilib;
}