package org.example.ilib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Register extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
        primaryStage.setTitle("Register");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("Register.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
