package com.karmanno.verificator.gui;

import com.karmanno.verificator.log.LogHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    private LogHandler logHandler;

    @FXML
    Button startVerificationButton;

    @FXML
    TextArea logsTextArea;

    @FXML
    public void handleOnStartVerificationClicked(MouseEvent mouseEvent) {
        System.out.println("Mouse clicked");
        startVerificationButton.setDisable(true);
        logHandler.log("Hi there!");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logHandler = new LogHandler(logsTextArea);
    }
}
