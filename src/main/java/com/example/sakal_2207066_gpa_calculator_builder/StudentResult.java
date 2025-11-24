package com.example.sakal_2207066_gpa_calculator_builder;

public class StudentResult {
    private int id;
    private String name;
    private double gpa;
    private String date;

    public StudentResult() {
    }

    public StudentResult(int id, String name, double gpa, String date) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}