package com.example.sakal_2207066_gpa_calculator_builder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CourseStore {
    public static final ObservableList<Course> courses = FXCollections.observableArrayList();
    public static int totalCredit = 0;

    public static int currentCredits() {
        return courses.stream().mapToInt(Course::getCredit).sum();
    }

    public static double computeGPA() {
        double sumWeighted = courses.stream().mapToDouble(c -> c.getCredit() * c.getGradePoint()).sum();
        int total = totalCredit;
        if (total == 0) return 0.0;
        return sumWeighted / total;
    }

    public static void clearAll() {
        courses.clear();
        totalCredit = 0;
    }
}
