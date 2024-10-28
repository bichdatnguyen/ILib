package org.example.ilib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginAndRegister extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("LoginAndRegister.fxml"));
        primaryStage.setTitle("Login And Register");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("LoginAndRegister.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
