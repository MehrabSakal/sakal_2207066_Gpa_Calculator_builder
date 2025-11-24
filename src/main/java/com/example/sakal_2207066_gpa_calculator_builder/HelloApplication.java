package com.example.sakal_2207066_gpa_calculator_builder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {

    private FXMLLoader loader;

    @Override
    public void start(Stage stage) throws Exception {
        DatabaseHandler.initDB();

        loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 620, 550);

        try {
            scene.getStylesheets().add(Objects.requireNonNull(
                    HelloApplication.class.getResource("file.css")).toExternalForm()
            );
        } catch (Exception ignored) {}

        stage.setTitle("GPA Calculator & Manager");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        if (loader != null && loader.getController() != null) {
            Object controller = loader.getController();
            if (controller instanceof HelloController) {
                ((HelloController) controller).shutdownExecutor();
            }
        }
        super.stop();
    }
}