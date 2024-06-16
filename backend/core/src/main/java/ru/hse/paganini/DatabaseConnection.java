package ru.hse.paganini;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class DatabaseConnection {
    private static final int MAX_CONNECTIONS = 10;
    private static final String DB_URL = "jdbc:postgresql://database:5432/wordle";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "E9Nlyrjt";
    private static final Connection[] connections = new Connection[MAX_CONNECTIONS];
    private static int currentConnectionIndex = 0;
    private Map<Integer, Game> games = new HashMap<Integer, Game>();

    static {
        try {
            for (int i = 0; i < MAX_CONNECTIONS; i++) {
                connections[i] = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        Connection connection = connections[currentConnectionIndex];
        currentConnectionIndex = (currentConnectionIndex + 1) % MAX_CONNECTIONS;
        return connection;
    }

    public void initialize() {
        try {
            Connection connection = getConnection();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS words_usual (word TEXT)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS words_rare (word TEXT)").execute();
            try (Scanner scanner = new Scanner(new File("resources/words_174.txt"))) {
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    connection.prepareStatement("INSERT INTO words_usual (word) VALUES ('" + word + "')").execute();
                    connection.prepareStatement("INSERT INTO words_rare (word) VALUES ('" + word + "')").execute();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try (Scanner scanner = new Scanner(new File("resources/words_5742.txt"))) {
                while (scanner.hasNext()) {
                    String word = scanner.next();
                    connection.prepareStatement("INSERT INTO words_rare (word) VALUES ('" + word + "')").execute();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String get_new_word(String clientIP) {
        String word = "";
        try {
            word = getConnection().prepareStatement("SELECT word FROM words_usual ORDER BY RANDOM() LIMIT 1").executeQuery().getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return word;
    }
}
