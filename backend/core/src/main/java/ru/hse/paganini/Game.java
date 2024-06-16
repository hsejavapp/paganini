package ru.hse.paganini;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class Game {
    private Status status = Status.PROCESSING;
    private int attemptsCnt = 0;
    private String rightWord;
    DatabaseConnection database;

    public Game(DatabaseConnection database_) {
        database = database_;
    }
    
    public String getWord(String type_) {
        String dataset = type_.equals("usual") ? "words_usual" : "words_rare";
        try {
            ResultSet rs = database.getConnection().prepareStatement("SELECT word FROM " + dataset + " ORDER BY RANDOM() LIMIT 1").executeQuery();
            rightWord = rs.next() ? rs.getString("word") : null;
            return rightWord;
        } catch (SQLException e) {
            System.err.println("Query failed");
            e.printStackTrace();
            return null;
        }
    }

    public String getRightWord() {
        return rightWord;
    }

    private boolean validateWord(String word) {
        try {
            if (word.length() != 5)
                return false;
            return database.getConnection().prepareStatement("SELECT word FROM words_rare WHERE word = '" + word + "'").executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCollisions(String word) {
        // проверяем, что слово валидное
        if (!validateWord(word))
            return "";

        char[] collisions = "00000".toCharArray();
        System.err.println("Right word: " + rightWord);
        System.err.println("Your word: " + word);
        // сначала смотрим на зелёные буквы и удаляем их из слов
        List<Character> wordChars = word.chars().mapToObj(e->(char)e).collect(Collectors.toList());
        List<Character> rightWordChars = rightWord.chars().mapToObj(e->(char)e).collect(Collectors.toList());
        for (int i = 0; i < 5; i++) {
            if (rightWord.charAt(i) == word.charAt(i)) {
                System.out.println("Green letter: " + word.charAt(i));
                collisions[i] = '2';
                wordChars.set(i, null);
                rightWordChars.set(i, null);
            }
        }
        // теперь смотрим на оставшиеся буквы и ищем среди них жёлтые

        System.err.println(wordChars);
        System.err.println(rightWordChars);

        for (int i = 0; i < 5; i++) {
            if (wordChars.get(i) == null)
                continue;
            for (int j = 0; j < 5; j++) {
                if (rightWordChars.get(j) == null)
                    continue;
                if (rightWordChars.get(j).equals(wordChars.get(i))) {
                    collisions[i] = '1';
                    System.out.println("Yellow letter: " + word.charAt(i) + " with i = " + i + " and j = " + j);
                    rightWordChars.set(j, null);
                    wordChars.set(i, null);
                    break;
                }
            }
        }
        attemptsCnt++;
        System.err.println(collisions);
        System.err.println(new String(collisions));
        return new String(collisions);
    }
}
