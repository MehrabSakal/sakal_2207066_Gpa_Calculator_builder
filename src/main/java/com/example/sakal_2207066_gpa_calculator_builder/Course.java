package com.example.sakal_2207066_gpa_calculator_builder;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Course {
    private final SimpleStringProperty name;
    private final SimpleStringProperty code;
    private final SimpleIntegerProperty credit;
    private final SimpleStringProperty teacher1;
    private final SimpleStringProperty teacher2;
    private final SimpleStringProperty grade;
    private final SimpleDoubleProperty gradePoint;

    public Course(String name, String code, int credit, String teacher1, String teacher2, String grade, double gradePoint) {
        this.name = new SimpleStringProperty(name);
        this.code = new SimpleStringProperty(code);
        this.credit = new SimpleIntegerProperty(credit);
        this.teacher1 = new SimpleStringProperty(teacher1);
        this.teacher2 = new SimpleStringProperty(teacher2);
        this.grade = new SimpleStringProperty(grade);
        this.gradePoint = new SimpleDoubleProperty(gradePoint);
    }

    public String getName() { return name.get(); }
    public String getCode() { return code.get(); }
    public int getCredit() { return credit.get(); }
    public String getTeacher1() { return teacher1.get(); }
    public String getTeacher2() { return teacher2.get(); }
    public String getGrade() { return grade.get(); }
    public double getGradePoint() { return gradePoint.get(); }
}
