package com.karmanno.verificator.gui;

import com.karmanno.verificator.io.TextFileLoader;
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
    private TextFileLoader textFileLoader = new TextFileLoader();

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

    @FXML
    public void handleOnLoadFileButtonClicked(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open text file with accounts");
        File file = fileChooser.showOpenDialog(new Stage());

        if(file != null) {
            try {
                ArrayList<User> models = textFileLoader.loadModels(file);
                tableView.getColumns().clear();
                TableColumn<Map, String> usernameColumn = new TableColumn<>("Username");
                TableColumn<Map, String> passwordColumn = new TableColumn<>("Password");
                TableColumn<Map, String> statusColumn = new TableColumn<>("Status");

                usernameColumn.setCellFactory(new MapValueFactory("username"));
                passwordColumn.setCellFactory(new MapValueFactory("password"));
                statusColumn.setCellFactory(new MapValueFactory("status"));

                tableView.getColumns().addAll(usernameColumn, passwordColumn, statusColumn);

                tableView.getItems().clear();

                for(User model : models) {
                    Map<String, String> dataRow = new HashMap<>();
                    dataRow.put("username", model.getUsername());
                    dataRow.put("password", model.getPassword());
                    dataRow.put("status", model.getUserStatus().toString());
                    tableView.getItems().add(dataRow);
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
