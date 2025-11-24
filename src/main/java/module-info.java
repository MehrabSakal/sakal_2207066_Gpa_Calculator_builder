module com.example.sakal_2207066_gpa_calculator_builder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires com.fasterxml.jackson.databind;

    opens com.example.sakal_2207066_gpa_calculator_builder to javafx.fxml, com.fasterxml.jackson.databind;

    exports com.example.sakal_2207066_gpa_calculator_builder;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

}