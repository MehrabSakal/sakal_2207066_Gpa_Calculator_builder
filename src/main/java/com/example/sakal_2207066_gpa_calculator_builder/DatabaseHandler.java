package com.example.sakal_2207066_gpa_calculator_builder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

public class DatabaseHandler {

    // This string creates the .db file automatically
    private static final String DB_URL = "jdbc:sqlite:student_gpa.db";

    public static void initDB() {
        try {
            // Force load the driver to prevent "No suitable driver" error
            Class.forName("org.sqlite.JDBC");

            String sql = "CREATE TABLE IF NOT EXISTS results (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "gpa REAL, " +
                    "date TEXT)";

            try (Connection conn = DriverManager.getConnection(DB_URL);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
                System.out.println("Database initialized successfully.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite Driver NOT FOUND! Check pom.xml");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addResult(String name, double gpa) {
        String sql = "INSERT INTO results(name, gpa, date) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, gpa);
            pstmt.setString(3, LocalDate.now().toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<StudentResult> getAllResults() {
        ObservableList<StudentResult> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM results ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new StudentResult(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("gpa"),
                        rs.getString("date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void updateResult(int id, String newName, double newGpa) {
        String sql = "UPDATE results SET name = ?, gpa = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setDouble(2, newGpa);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteResult(int id) {
        String sql = "DELETE FROM results WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}