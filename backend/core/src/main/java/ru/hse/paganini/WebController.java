package ru.hse.paganini;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;


@Controller
public class WebController {
    private DatabaseConnection databaseConnection;
    private HashMap<String, Game> games = new HashMap<String, Game>();

    public WebController() {
        // Initialize database
        int attempts = 0;
        while (attempts < 10) {
            try {
                Thread.sleep(5000);
                databaseConnection = new DatabaseConnection();
                databaseConnection.initialize();
                System.err.println("Connection established");
                break;
            } catch (Exception e) {
                System.err.println("Connection failed");
                e.printStackTrace();
            }
            attempts++;
        }
        if (attempts == 10) {
            System.err.println("Failed to connect to the database");
            System.exit(1);
        }
    }

    @GetMapping("new_word")
    public ResponseEntity<String> newWord(Model model, HttpServletRequest request) {
        // Get client IP address
        String clientIP = request.getHeader("x-real-ip");
        System.out.println("Client IP: " + clientIP);
        
        // generate word
        if (!games.containsKey(clientIP)) {
            games.put(clientIP, new Game(databaseConnection));
        }
        String newWord = games.get(clientIP).getWord("usual");
        if (newWord == null) {
            return ResponseEntity.ok("HTTP/1.1 500 Internal Server Error\r\n\r\n");
        }
        return ResponseEntity.ok(newWord);
    }

    @GetMapping("right_word")
    public ResponseEntity<String> rightWord(Model model, HttpServletRequest request) {
        String clientIP = request.getHeader("x-real-ip");
        System.out.println("Client IP: " + clientIP);
        if (!games.containsKey(clientIP)) {
            return null;
        }
        String rightWord = games.get(clientIP).getRightWord();
        return ResponseEntity.ok(rightWord);
    }

    @GetMapping("/collisions/{id}")
    public ResponseEntity<String> collision(@PathVariable String id, Model model, HttpServletRequest request) {
        String clientIP = request.getHeader("x-real-ip");
        System.out.println("Client IP: " + clientIP);
        if (!games.containsKey(clientIP)) {
            return null;
        }
        String collisions = games.get(clientIP).getCollisions(id) == null ? "" : games.get(clientIP).getCollisions(id);
        return ResponseEntity.ok(collisions);
    }
}
