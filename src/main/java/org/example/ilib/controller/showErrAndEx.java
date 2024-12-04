package org.example.ilib.controller;

import javafx.scene.control.Alert;

public class showErrAndEx {
    public static Alert showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.showAndWait();
        return alert;
    }

}
