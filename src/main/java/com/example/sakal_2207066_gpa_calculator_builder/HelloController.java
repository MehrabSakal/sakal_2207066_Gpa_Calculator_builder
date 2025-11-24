package com.example.sakal_2207066_gpa_calculator_builder;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloController {


    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @FXML private Label creditlabel;
    @FXML private TextField creditamount;
    @FXML private Button calculationstart;
    @FXML private Label currentCreditLabel;
    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField creditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private TextField gradeField;
    @FXML private Button addCourseButton;
    @FXML private Button calculateid;
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, String> colCode;
    @FXML private TableColumn<Course, Integer> colCredit;
    @FXML private TableColumn<Course, String> colTeacher1;
    @FXML private TableColumn<Course, String> colTeacher2;
    @FXML private TableColumn<Course, String> colGrade;
    @FXML private TextArea resultSummary;
    @FXML private Label gpaLabel;
    @FXML private Button restartButton;

    @FXML private TableView<Course> summaryTable;
    @FXML private TableColumn<Course, String> sumColName;
    @FXML private TableColumn<Course, String> sumColCode;
    @FXML private TableColumn<Course, String> sumColGrade;
    @FXML private Label totalCreditsLabel;

    @FXML private TextField studentNameInput;
    @FXML private TextField studentGpaInput;
    @FXML private TableView<StudentResult> dbTable;
    @FXML private TableColumn<StudentResult, Integer> colDbId;
    @FXML private TableColumn<StudentResult, String> colDbName;
    @FXML private TableColumn<StudentResult, Double> colDbGpa;
    @FXML private TableColumn<StudentResult, String> colDbDate;

    @FXML
    private void initialize() {
        try {
            if (courseTable != null) {
                if (colName != null) colName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
                if (colCode != null) colCode.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCode()));
                if (colCredit != null) colCredit.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCredit()).asObject());
                if (colTeacher1 != null) colTeacher1.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTeacher1()));
                if (colTeacher2 != null) colTeacher2.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTeacher2()));
                if (colGrade != null) colGrade.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGrade()));
                refreshTable();
            }
        } catch (Exception ignored) {}

        try {
            if (gpaLabel != null) {
                double gpa = CourseStore.computeGPA();
                gpaLabel.setText(String.format("%.2f", gpa));

                if (studentGpaInput != null) studentGpaInput.setText(String.format("%.2f", gpa));

                if (summaryTable != null) {
                    sumColName.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
                    sumColCode.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCode()));
                    sumColGrade.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGrade()));
                    summaryTable.setItems(CourseStore.courses);
                }

                if (totalCreditsLabel != null) totalCreditsLabel.setText("Total Credits: " + CourseStore.totalCredit);

                if (colDbId != null) {
                    colDbId.setCellValueFactory(new PropertyValueFactory<>("id"));
                    colDbName.setCellValueFactory(new PropertyValueFactory<>("name"));
                    colDbGpa.setCellValueFactory(new PropertyValueFactory<>("gpa"));
                    colDbDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                }

                loadDatabaseTable();

                if (dbTable != null) {
                    dbTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            studentNameInput.setText(newSelection.getName());
                            studentGpaInput.setText(String.valueOf(newSelection.getGpa()));
                        }
                    });
                }
            }
        } catch (Exception ignored) {}

        try {
            if (calculateid != null) checkEnableCalculate();
        } catch (Exception ignored) {}
    }


    private void loadDatabaseTable() {
        if (dbTable == null) return;

        dbTable.setDisable(true);

        executor.submit(() -> {
            ObservableList<StudentResult> data = DatabaseHandler.getAllResults();

            Platform.runLater(() -> {
                dbTable.setItems(data);
                dbTable.setDisable(false);
            });
        });
    }

    @FXML
    protected void onSaveToDB() {
        String name = studentNameInput.getText();
        if (name == null || name.trim().isEmpty()) {
            showAlert("Error", "Please enter a student name.");
            return;
        }
        double currentGpa = CourseStore.computeGPA();

        executor.submit(() -> {
            DatabaseHandler.addResult(name, currentGpa);

            Platform.runLater(() -> {
                loadDatabaseTable();
                studentNameInput.clear();
                showAlert("Success", "Saved to JSON file!");
            });
        });
    }

    @FXML
    protected void onUpdateDbClick() {
        StudentResult selected = dbTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selection Error", "Please select a row to update.");
            return;
        }

        String newName = studentNameInput.getText();
        String newGpaText = studentGpaInput.getText();

        if (newName == null || newName.isEmpty() || newGpaText == null || newGpaText.isEmpty()) return;

        try {
            double newGpa = Double.parseDouble(newGpaText);

            executor.submit(() -> {
                DatabaseHandler.updateResult(selected.getId(), newName, newGpa);

                Platform.runLater(() -> {
                    loadDatabaseTable();
                    studentNameInput.clear();
                    studentGpaInput.clear();
                });
            });

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid GPA format.");
        }
    }

    @FXML
    protected void onDeleteDbClick() {
        StudentResult selected = dbTable.getSelectionModel().getSelectedItem();
        if (selected != null) {

            // Run in Background Thread
            executor.submit(() -> {
                DatabaseHandler.deleteResult(selected.getId());

                Platform.runLater(() -> {
                    loadDatabaseTable();
                    studentNameInput.clear();
                    studentGpaInput.clear();
                });
            });
        }
    }

    public void shutdownExecutor() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    private double gradeToPoint(String grade) {
        if (grade == null) return 0.0;
        grade = grade.trim().toUpperCase(Locale.ROOT);
        switch (grade) {
            case "A+": return 4.0; case "A": return 3.75; case "A-": return 3.5;
            case "B+": return 3.25; case "B": return 3.0; case "B-": return 2.75;
            case "C+": return 2.50; case "C": return 2.25; case "D": return 2.0;
            case "F": return 0.0;
            default: try { return Double.parseDouble(grade); } catch (Exception e) { return 0.0; }
        }
    }

    private void showAlert(String title, String msg) {
        // Alerts must run on UI Thread
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle(title);
            a.setHeaderText(null);
            a.setContentText(msg);
            a.showAndWait();
        });
    }

    @FXML protected void onStartGpaClick() { loadScene("first-view.fxml", calculationstart); }

    @FXML protected void onEnterButtonClick() {
        try {
            int total = Integer.parseInt(creditamount.getText().trim());
            if (total <= 0) throw new NumberFormatException();
            CourseStore.clearAll();
            CourseStore.totalCredit = total;
            loadScene("second-view.fxml", creditlabel);
        } catch (Exception ex) { showAlert("Error", "Enter valid total credit"); }
    }

    @FXML protected void OnAddCourseClick() {
        String name = safeText(courseNameField);
        String code = safeText(courseCodeField);
        String cr = safeText(creditField);
        String t1 = safeText(teacher1Field);
        String t2 = safeText(teacher2Field);
        String gradeTxt = safeText(gradeField);

        if (name.isEmpty() || code.isEmpty() || cr.isEmpty() || gradeTxt.isEmpty()) {
            showAlert("Missing fields", "Fill Name, Code, Credit, Grade"); return;
        }
        try {
            int creditVal = Integer.parseInt(cr);
            if (CourseStore.currentCredits() + creditVal > CourseStore.totalCredit) {
                showAlert("Error", "Credit Limit Exceeded"); return;
            }
            CourseStore.courses.add(new Course(name, code, creditVal, t1, t2, gradeTxt, gradeToPoint(gradeTxt)));
            refreshTable();
            checkEnableCalculate();
            courseNameField.clear(); courseCodeField.clear(); creditField.clear();
            teacher1Field.clear(); teacher2Field.clear(); gradeField.clear();
        } catch (Exception e) { showAlert("Error", "Invalid Credit"); }
    }

    @FXML protected void OnCalculateButtonClick() {
        if (CourseStore.currentCredits() != CourseStore.totalCredit) {
            showAlert("Error", "Credits must match total"); return;
        }
        loadScene("third-view.fxml", addCourseButton);
    }

    @FXML protected void onRestartClick() {
        CourseStore.clearAll();
        loadScene("hello-view.fxml", restartButton);
    }

    private void loadScene(String fxml, Control c) {
        try {
            Stage stage = (Stage) c.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)))));
            stage.show();
        } catch (IOException e) { e.printStackTrace(); }
    }

    private String safeText(TextField f) { return (f == null || f.getText() == null) ? "" : f.getText().trim(); }

    private void refreshTable() {
        if (courseTable != null) courseTable.setItems(CourseStore.courses);
        if (currentCreditLabel != null) currentCreditLabel.setText(String.valueOf(CourseStore.currentCredits()));
    }

    private void checkEnableCalculate() {
        if (calculateid != null) calculateid.setDisable(CourseStore.currentCredits() != CourseStore.totalCredit);
    }
}