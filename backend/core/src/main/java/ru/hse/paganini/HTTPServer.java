package ru.hse.paganini;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ru.hse.paganini.Game;


public class HTTPServer {
    private static Connection connection;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port, 0, InetAddress.getByName("0.0.0.0"));
            System.out.println("Server started on port " + port);
            connection = getConnection();
            System.out.println(connection == null ? "Connection is null" : "Connection is not null");
            System.out.println("First check");
            initializeDatabase();
            Map<Integer, Game> games = new HashMap<Integer, Game>();

            while (true) {
                System.out.println("Waiting for client connection...");
                clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String request = in.readLine();
                System.out.println("Received request: " + request);
                if (request.startsWith("GET /new_word") || request.startsWith("GET /new_word")) {
                    int gameId = clientSocket.getInetAddress().hashCode();
                    Game game = new Game(connection);
                    String word = game.getRightWord();
                    games.put(gameId, game);
                    if (word == null) {
                        word = "Error";
                    }
                    String response = "HTTP/1.1 200 OK\r\n\r\n" + word + "\n";
                    clientSocket.getOutputStream().write(response.getBytes());
                    clientSocket.getOutputStream().flush();
                    clientSocket.close();
                    System.out.println("Client connection closed");
                    continue;
                }
                if (request.startsWith("GET /collisions")) {
                    String word = request.split(" ")[1].substring(12);
                    word = java.net.URLDecoder.decode(word, "UTF-8");
                    System.out.println("Received word: " + word);

                    int gameId = clientSocket.getInetAddress().hashCode();
                    Game game = games.get(gameId);
                    if (game == null) {
                        System.out.println("No game for client " + clientSocket.getInetAddress());
                        continue;
                        // TODO: написать ответ клиенту, что игра не начата
                    }

                    System.out.println("Game started");
                    String response = "HTTP/1.1 200 OK\r\n\r\n" + game.getCollisions(word) +"\n";

                    clientSocket.getOutputStream().write(response.getBytes());
                    clientSocket.getOutputStream().flush();
                    clientSocket.close();
                    System.out.println("Client connection closed");
                    continue;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            if (in != null) {
                in.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() {
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS words (word VARCHAR(255) PRIMARY KEY)").execute();
            connection.prepareStatement("INSERT INTO words (word) VALUES ('арбуз')").execute();
            connection.prepareStatement("INSERT INTO words (word) VALUES ('банан')").execute();
            connection.prepareStatement("INSERT INTO words (word) VALUES ('вишня')").execute();
        } catch (SQLException e) {
            System.err.println("Query failed");
            e.printStackTrace();
        }
    }

    private static Connection getConnection() {
        try {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
            return DriverManager.getConnection("jdbc:postgresql://database:5432/wordle", "postgres", "123");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

