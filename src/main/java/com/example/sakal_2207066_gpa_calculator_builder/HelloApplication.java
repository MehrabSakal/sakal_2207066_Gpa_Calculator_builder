package com.example.sakal_2207066_gpa_calculator_builder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 620, 440);
        scene.getStylesheets().add(Objects.requireNonNull(
                HelloApplication.class.getResource("file.css")).toExternalForm()
        );
        stage.setTitle("GPA Calculator Builder");
        stage.setScene(scene);
        stage.show();
    }
}
