package org.example.ilib.Controller;

import javafx.scene.control.Alert;

public class showErrAndEx {
    public static void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
