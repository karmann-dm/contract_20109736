package com.karmanno.verificator.gui;

import javafx.scene.control.Alert;

public class AlertFactory {
    public Alert createAlert(String title, String headerText, Alert.AlertType alertType, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        return alert;
    }
}
