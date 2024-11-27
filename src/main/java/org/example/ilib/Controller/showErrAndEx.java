package org.example.ilib.Controller;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;

public class showErrAndEx {
    public static Alert showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.showAndWait();
        return alert;
    }

}
