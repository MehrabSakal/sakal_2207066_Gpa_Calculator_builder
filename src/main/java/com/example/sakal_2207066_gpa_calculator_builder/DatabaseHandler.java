package com.example.sakal_2207066_gpa_calculator_builder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler {

    private static final String FILE_PATH = "gpa_data.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void initDB() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            try {
                mapper.writeValue(file, new ArrayList<StudentResult>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addResult(String name, double gpa) {
        List<StudentResult> list = loadFromFile();
        int newId = list.stream().mapToInt(StudentResult::getId).max().orElse(0) + 1;
        list.add(new StudentResult(newId, name, gpa, LocalDate.now().toString()));
        saveToFile(list);
    }

    public static ObservableList<StudentResult> getAllResults() {
        return FXCollections.observableArrayList(loadFromFile());
    }

    public static void updateResult(int id, String newName, double newGpa) {
        List<StudentResult> list = loadFromFile();
        for (StudentResult s : list) {
            if (s.getId() == id) {
                s.setName(newName);
                s.setGpa(newGpa);
                break;
            }
        }
        saveToFile(list);
    }

    public static void deleteResult(int id) {
        List<StudentResult> list = loadFromFile();
        list.removeIf(s -> s.getId() == id);
        saveToFile(list);
    }

    private static List<StudentResult> loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return new ArrayList<>();
        try {
            return mapper.readValue(file, new TypeReference<List<StudentResult>>(){});
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static void saveToFile(List<StudentResult> list) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}