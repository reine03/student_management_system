package com.student;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class StudentManagementApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL fxmlUrl = getClass().getResource("/com/student/main.fxml");
        if (fxmlUrl == null) {
            throw new IllegalStateException("Could not find /com/student/main.fxml");
        }

        URL cssUrl = getClass().getResource("/com/student/style.css");
        if (cssUrl == null) {
            throw new IllegalStateException("Could not find /com/student/style.css");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(cssUrl.toExternalForm());

        stage.setTitle("Student Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
