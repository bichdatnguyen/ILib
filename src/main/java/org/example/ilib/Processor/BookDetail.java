package org.example.ilib.Processor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ilib.Controller.ControllerBookDetail;

import java.io.IOException;

public class BookDetail extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/ilib/bookDetail.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Book detail");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

//        ControllerBookDetail controller = loader.getController();
//        controller.setInformation();
    }
}
