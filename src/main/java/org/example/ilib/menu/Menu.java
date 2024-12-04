package org.example.ilib.menu;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Menu extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * this method will be called when user login or register successfully.
     *
     * @param primaryStage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException in case FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/ilib/Menu.fxml"));
        primaryStage.setTitle("Menu");
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