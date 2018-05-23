package com.karmanno.verificator.gui;

import com.karmanno.verificator.io.TextFileLoader;
import com.karmanno.verificator.log.LogHandler;
import com.karmanno.verificator.model.User;
import com.karmanno.verificator.model.UserStatus;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
    Button clearAllButton;

    @FXML
    TextArea logsTextArea;

    @FXML
    Button loadTextFileButton;

    @FXML
    TableView tableView;

    @FXML
    ProgressBar progressBar;

    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for(int i = 0; i < 1000000; i++)
                    System.out.println("ddd");
                return new Object();
            }
        };
    }

    @FXML
    public void handleOnStartVerificationClicked(MouseEvent mouseEvent) {
        logHandler.log("Verification started");
        startVerificationButton.setDisable(true);
        Task task = createWorker();
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            progressBar.progressProperty().unbind();
            progressBar.setProgress(1.0);
            startVerificationButton.setDisable(false);
        });
        task.setOnFailed(e -> task.getException().printStackTrace());
        Thread thread = new Thread(task);
        thread.start();
    }

    @FXML
    public void handleOnClearAllButtonClicked(MouseEvent mouseEvent) {
        logHandler.log("Clearing table...");
        tableView.getItems().clear();
        tableView.getColumns().clear();
        logHandler.log("Table is clear");
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
