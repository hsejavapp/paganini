package ru.hse.paganini;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;


public class Game {
    public Connection connection;
    private Status status = Status.PROCESSING;
    private int attemptsCnt = 0;
    private String rightWord;

    public Game(Connection connection) {
        this.connection = connection;
        rightWord = getWord();
    }
    
    public String getWord() {
        try {
            ResultSet rs = connection.prepareStatement("SELECT word FROM words ORDER BY RANDOM() LIMIT 1").executeQuery();
            return rs.next() ? rs.getString("word") : null;
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
            return connection.prepareStatement("SELECT word FROM words WHERE word = '" + word + "'").executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getCollisions(String word) {
        // хз, какие правила у игры, но я напишу так, как я их представляю

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
                } else {
                    System.err.println(rightWordChars.get(j) + " != " + wordChars.get(i));
                }
            }
        }
        attemptsCnt++;
        System.err.println(collisions);
        System.err.println(new String(collisions));
        return new String(collisions);
    }

    public Status sendStatus() {
        // кидаем DEFEAT или VICTORY, чтобы показать в приложении окошко
        // TODO: сделать это))))
        return null;
    }    
}


