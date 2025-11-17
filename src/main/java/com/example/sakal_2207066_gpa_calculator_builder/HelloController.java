package com.example.sakal_2207066_gpa_calculator_builder;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;

public class HelloController {


    @FXML private Label welcomeText;
    @FXML private Button calculationstart;


    @FXML private Label creditlabel;
    @FXML private TextField creditamount;
    @FXML private Button creditbutton;
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


    private double gradeToPoint(String grade) {
        if (grade == null) return 0.0;
        grade = grade.trim().toUpperCase(Locale.ROOT);
        switch (grade) {
            case "A+": return 4.0;
            case "A":  return 3.75;
            case "A-": return 3.5;
            case "B+": return 3.25;
            case "B":  return 3.0;
            case "B-": return 2.75;
            case "C+": return 2.50;
            case "C":  return 2.25;
            case "D":  return 2.0;
            case "F":  return 0.0;
            default:

                try { return Double.parseDouble(grade); }
                catch (Exception e) { return 0.0; }
        }
    }


    @FXML
    protected void onStartGpaClick() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("first-view.fxml")));
            Stage stage = (Stage) calculationstart.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void onEnterButtonClick() {
        String t = creditamount.getText();
        int total;
        try {
            total = Integer.parseInt(t.trim());
            if (total <= 0) throw new NumberFormatException();
        } catch (Exception ex) {
            showAlert("Invalid total credit", "Please enter a positive integer for total credit.");
            return;
        }
        CourseStore.clearAll();
        CourseStore.totalCredit = total;

        try {
            Parent root1 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("second-view.fxml")));
            Stage stage = (Stage) creditlabel.getScene().getWindow();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    protected void OnAddCourseClick() {
        String name = safeText(courseNameField);
        String code = safeText(courseCodeField);
        String cr = safeText(creditField);
        String t1 = safeText(teacher1Field);
        String t2 = safeText(teacher2Field);
        String gradeTxt = safeText(gradeField);

        if (name.isEmpty() || code.isEmpty() || cr.isEmpty() || gradeTxt.isEmpty()) {
            showAlert("Missing fields", "Please enter Course Name, Code, Credit and Grade at minimum.");
            return;
        }

        int creditVal;
        try {
            creditVal = Integer.parseInt(cr);
            if (creditVal <= 0) throw new NumberFormatException();
        } catch (Exception ex) {
            showAlert("Invalid credit", "Credit must be a positive integer.");
            return;
        }

        if (CourseStore.currentCredits() + creditVal > CourseStore.totalCredit) {
            showAlert("Credit overflow", "Adding this course would exceed the total credit of " + CourseStore.totalCredit);
            return;
        }

        double gp = gradeToPoint(gradeTxt);
        Course c = new Course(name, code, creditVal, t1, t2, gradeTxt, gp);
        CourseStore.courses.add(c);
        refreshTable();


        courseNameField.clear();
        courseCodeField.clear();
        creditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeField.clear();

        checkEnableCalculate();
    }

    private String safeText(TextField f) {
        return (f == null) ? "" : f.getText() == null ? "" : f.getText().trim();
    }

    private void refreshTable() {
        if (courseTable != null) {
            courseTable.setItems(CourseStore.courses);
        }
        if (currentCreditLabel != null) {
            currentCreditLabel.setText(String.valueOf(CourseStore.currentCredits()));
        }
    }


    private void checkEnableCalculate() {
        if (calculateid != null) {
            calculateid.setDisable(CourseStore.currentCredits() != CourseStore.totalCredit);
        }
    }

    @FXML
    protected void OnCalculateButtonClick() {
        if (CourseStore.currentCredits() != CourseStore.totalCredit) {
            showAlert("Incomplete credits", "Total entered credits must equal the initial total credit (" + CourseStore.totalCredit + ").");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(Objects.requireNonNull(getClass().getResource("third-view.fxml")));
            Parent root2 = loader.load();
            Stage stage = (Stage) addCourseButton.getScene().getWindow();
            stage.setScene(new Scene(root2));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                gpaLabel.setText(String.format("GPA: %.2f", gpa));
                StringBuilder sb = new StringBuilder();
                sb.append("Total credit: ").append(CourseStore.totalCredit).append("\n\n");
                ObservableList<Course> list = CourseStore.courses;
                for (Course c : list) {
                    sb.append(String.format("%s (%s) - %d cr - %s - %s - [%s]\n",
                            c.getName(), c.getCode(), c.getCredit(), c.getTeacher1(), c.getTeacher2(), c.getGrade()));
                }
                resultSummary.setText(sb.toString());
            }
        } catch (Exception ignored) {}

        try {
            if (calculateid != null) checkEnableCalculate();
        } catch (Exception ignored) {}
    }

    @FXML
    protected void onRestartClick() {
        CourseStore.clearAll();
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hello-view.fxml")));
            Stage stage = (Stage) restartButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
