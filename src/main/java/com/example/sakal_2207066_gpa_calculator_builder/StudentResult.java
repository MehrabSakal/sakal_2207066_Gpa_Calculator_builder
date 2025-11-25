package com.example.sakal_2207066_gpa_calculator_builder;

public class StudentResult {
    private int id;
    private String name;
    private double gpa;
    private String date;

    public StudentResult(int id, String name, double gpa, String date) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.date = date;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getGpa() { return gpa; }
    public String getDate() { return date; }
}