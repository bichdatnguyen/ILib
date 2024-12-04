package org.example.ilib.menu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Chatbot extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/ilib/Chatbot.fxml"));
        primaryStage.setTitle("Chatbot");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        // Tắt ExecutorService khi ứng dụng đóng
        ControllerMenu.shutdownExecutorService();
        System.out.println("Ứng dụng đã đóng và ExecutorService đã tắt.");
        System.exit(0);
        Platform.exit();
    }
}
