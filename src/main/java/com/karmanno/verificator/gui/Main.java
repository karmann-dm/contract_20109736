package com.karmanno.verificator.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("mainForm.fxml"));
        primaryStage.setTitle("Account verificator");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setMinWidth(400.0);
        primaryStage.setMinHeight(300.0);
        primaryStage.show();
    }


    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        System.setProperty("webdriver.gecko.driver", "src/main/resources/geckodriver.exe");
        System.setProperty("webdriver.opera.driver", "src/main/resources/operadriver.exe");
        launch(args);
    }
}
