package com.karmanno.verificator.gui;

import com.karmanno.verificator.io.TextFileLoader;
import com.karmanno.verificator.log.LogHandler;
import com.karmanno.verificator.model.User;
import com.karmanno.verificator.model.UserStatus;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    private LogHandler logHandler;
    private TextFileLoader textFileLoader = new TextFileLoader();
    private AlertFactory alertFactory = new AlertFactory();
    ArrayList<User> models = null;

    @FXML
    Button startVerificationButton;

    @FXML
    Button clearAllButton;

    @FXML
    TextArea logsTextArea;

    @FXML
    Button loadTextFileButton;

    @FXML
    Button exportVerifiedContactsButton;

    @FXML
    TableView tableView;

    @FXML
    ProgressBar progressBar;

    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                int index = 0;
                for(User model : models) {
                    model.setUserStatus(UserStatus.VERIFYING);
                    tableView.getItems().set(index, model);

                    try {
                        OperaOptions operaOptions = new OperaOptions();
                        operaOptions.setBinary("C:\\Program Files\\Opera\\52.0.2871.64\\opera.exe");

                        WebDriver webDriver = new OperaDriver(operaOptions);
                        webDriver.get("https://www.nike.com/gb/en_gb/gb/");

                        Thread.sleep(1000);

                        WebElement body = ((OperaDriver) webDriver).findElementByTagName("body");
                        Actions builder = new Actions(webDriver);
                        builder.moveToElement(body, 50, 50).click().build().perform();

                        Thread.sleep(3000);

                        WebElement acceptButton = webDriver.findElement(By.cssSelector("button[class='nsg-button nsg-grad--nike-orange yes-button cookie-settings-button js-yes-button wide']"));
                        builder.moveToElement(acceptButton, 0, 0).click().build().perform();

                        Thread.sleep(1000);

                        WebElement loginButton = webDriver.findElement(By.cssSelector("span[class='login-text']"));
                        builder.moveToElement(loginButton, 0, 0).click().build().perform();

                        Thread.sleep(1000);

                        WebElement usernameInput = webDriver.findElement(By.cssSelector("input[type='email']"));
                        WebElement passwordInput = webDriver.findElement(By.cssSelector("input[type='password']"));

                        usernameInput.click();
                        usernameInput.clear();
                        usernameInput.sendKeys(model.getUsername());

                        passwordInput.click();
                        passwordInput.clear();
                        passwordInput.sendKeys(model.getPassword());

                        Thread.sleep(1000);

                        WebElement submitLoginButton = webDriver.findElement(By.cssSelector("input[type='button'][value='LOG IN']"));
                        submitLoginButton.click();

                        model.setUserStatus(UserStatus.VERIFIED);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logHandler.log("Failed to verify user " + model.toString());
                        model.setUserStatus(UserStatus.UNVERIFIED);
                    }
                    //Thread.sleep(1000);

                    tableView.getItems().set(index, model);
                    updateProgress(index, models.size());

                    index++;
                }

                return new Object();
            }
        };
    }

    @FXML
    public void handleOnStartVerificationClicked(MouseEvent mouseEvent) {
        if(models == null) {
            alertFactory.createAlert(
                    "Error",
                    "There are no accounts to verify",
                    Alert.AlertType.WARNING,
                    "There are no accounts to verify"
            ).showAndWait();
            return;
        }

        logHandler.log("Verification started");
        startVerificationButton.setDisable(true);
        Task task = createWorker();
        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(e -> {
            progressBar.progressProperty().unbind();
            progressBar.setProgress(1.0);
            startVerificationButton.setDisable(false);
            logHandler.log("Verification ended");
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
        if(models != null) {
            models.clear();
        }
        logHandler.log("Table is clear");
    }

    @FXML
    public void handleOnLoadFileButtonClicked(MouseEvent mouseEvent) {
        logHandler.log("Loading started...");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open text file with accounts");
        File file = fileChooser.showOpenDialog(new Stage());

        if(file != null) {
            try {
                if(models != null)
                    models.clear();

                models = textFileLoader.loadModels(file);

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
                alertFactory.createAlert(
                        "Error while opening file",
                        "Something went wrong",
                        Alert.AlertType.ERROR, e.getMessage()
                ).showAndWait();
            }
            finally {
                logHandler.log("Loading ended, loaded " + (models == null ? "0" : models.size()) + " entries");
            }
        }
        else {
            logHandler.log("File is null");
        }
    }

    @FXML
    public void handleOnExportVerifiedContactsButtonClicked(MouseEvent mouseEvent) {
        if(models == null) {
            alertFactory.createAlert(
                    "Error",
                    "There are no accounts to export",
                    Alert.AlertType.WARNING,
                    "There are no accounts to export"
            ).showAndWait();
            return;
        }
        if(models.stream().filter(user -> user.getUserStatus().equals(UserStatus.VERIFIED)).count() == 0) {
            alertFactory.createAlert(
                    "Error",
                    "There are no verified accounts",
                    Alert.AlertType.WARNING,
                    "There are no verified accounts"
            ).showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export verified contacts");
        fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        File file = fileChooser.showSaveDialog(new Stage());

        if(file != null) {
            try {
                textFileLoader.saveModels(
                        file,
                        models.stream().filter(
                                user -> user.getUserStatus().equals(UserStatus.VERIFIED)
                        ).collect(Collectors.toList()));
            } catch (Exception e) {
                alertFactory.createAlert(
                        "Error while opening file",
                        "Something went wrong",
                        Alert.AlertType.ERROR, e.getMessage()
                ).showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logHandler = new LogHandler(logsTextArea);
    }
}
