package com.karmanno.verificator.gui;

import autoitx4java.AutoItX;
import com.jacob.com.LibraryLoader;
import com.karmanno.verificator.automation.ChromeNikeAutomation;
import com.karmanno.verificator.automation.NikeAutomation;
import com.karmanno.verificator.io.ProxyFileLoader;
import com.karmanno.verificator.io.TextFileLoader;
import com.karmanno.verificator.log.LogHandler;
import com.karmanno.verificator.model.User;
import com.karmanno.verificator.model.UserStatus;
import com.karmanno.verificator.net.SmspvaApiWorker;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController implements Initializable {
    private LogHandler logHandler;
    private TextFileLoader textFileLoader = new TextFileLoader();
    private ProxyFileLoader proxyFileLoader = new ProxyFileLoader();
    private AlertFactory alertFactory = new AlertFactory();

    private ArrayList<Proxy> proxies = null;
    private ArrayList<User> models = null;
    private NikeAutomation automation;

    private SmspvaApiWorker smspvaApiWorker = new SmspvaApiWorker();

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

    private void mozProxyAuth(String login, String password) {
        String jacobDllToUse = System.getProperty("sun.arch.data.model").contains("32") ? "jacob-1.18-x86.dll" : "jacob-1.18-x64.dll";
        File file = new File(System.getProperty("user.dir"), jacobDllToUse);
        System.setProperty(LibraryLoader.JACOB_DLL_PATH, file.getAbsolutePath());
        AutoItX x = new AutoItX();
    }

    public Task createWorker() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                int index = 0;
                for(User model : models) {
                    model.setUserStatus(UserStatus.VERIFYING);
                    tableView.getItems().set(index, model);

                    Proxy proxy = proxies.get(0);

                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.setCapability("proxy", proxy);

                    ChromeDriver webDriver = new ChromeDriver(chromeOptions);

                    try {
                        ChromeNikeAutomation automation = new ChromeNikeAutomation(webDriver);

                        Process myProcess = Runtime.getRuntime().exec("src/main/resources/chromeProxy.exe");
                        automation.get();
                        myProcess.destroy();
                        myProcess.waitFor();

                        automation.clickAcceptCookies();
                        automation.clickLogin();
                        automation.fulfillForm(model.getUsername(), model.getPassword());
                        automation.getSettings();
                        automation.clickAddPhone();
                        automation.checkLegalTerms();

                        logHandler.log("Trying to get number...");
                        JSONObject number = smspvaApiWorker.getPhoneNumber();
                        if(number == null) {
                            logHandler.log("Cannot get the number");
                            throw new RuntimeException("Cannot get the number");
                        }
                        logHandler.log("Got " + number.getString("number") + " number");
                        automation.enterPhone(number.getString("number"));
                        automation.pressVerify();
                        automation.pressGetCode();

                        logHandler.log("Trying to get code...");
                        String code = smspvaApiWorker.getCode(number.get("id").toString());
                        if(code == null) {
                            logHandler.log("Cannot get the code");
                            throw new RuntimeException("Cannot get the code");
                        }
                        automation.enterCode(code);

                        model.setUserStatus(UserStatus.VERIFIED);

                    } catch (Exception e) {
                        e.printStackTrace();
                        logHandler.log("Failed to verify user " + model.toString());
                        model.setUserStatus(UserStatus.UNVERIFIED);
                    }

                    webDriver.close();

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

                models = textFileLoader.load(file);

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
                textFileLoader.save(
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
        try {
            proxies = proxyFileLoader.load(new File("src/main/resources/proxyList.txt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
