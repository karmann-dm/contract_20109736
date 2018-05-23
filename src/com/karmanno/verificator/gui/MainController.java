package com.karmanno.verificator.gui;

import com.karmanno.verificator.io.TextFileLoader;
import com.karmanno.verificator.log.LogHandler;
import com.karmanno.verificator.model.User;
import com.karmanno.verificator.model.UserStatus;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    private LogHandler logHandler;
    private TextFileLoader textFileLoader = new TextFileLoader();

    @FXML
    Button startVerificationButton;

    @FXML
    TextArea logsTextArea;

    @FXML
    Button loadTextFileButton;

    @FXML
    TableView tableView;

    @FXML
    public void handleOnStartVerificationClicked(MouseEvent mouseEvent) {
        startVerificationButton.setDisable(true);
    }

    @FXML
    public void handleOnLoadFileButtonClicked(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open text file with accounts");
        File file = fileChooser.showOpenDialog(new Stage());

        if(file != null) {
            try {
                ArrayList<User> models = textFileLoader.loadModels(file);
                tableView.getColumns().clear();

                TableColumn usernameColumn = new TableColumn("Username");
                usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
                TableColumn passwordColumn = new TableColumn("Password");
                passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
                TableColumn statusColumn = new TableColumn("Status");
                statusColumn.setCellValueFactory(new PropertyValueFactory<>("userStatus"));

                tableView.getColumns().addAll(usernameColumn, passwordColumn, statusColumn);

                for(User model : models) {
                    tableView.getItems().add(model);
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error while opening file");
                alert.setHeaderText("Something went wrong");

                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logHandler = new LogHandler(logsTextArea);
    }
}
