package ru.hse.paganini.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ru.hse.paganini.ConfigParser;


public class PostgreSQL {
    private Connection connection;

    public PostgreSQL() {
        String url = ConfigParser.getBotParameter("resources/postgres.yaml", "url");
        String username = ConfigParser.getBotParameter("resources/postgres.yaml", "user");
        String password = ConfigParser.getBotParameter("resources/postgres.yaml", "password");

        try {
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null) {
                System.err.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.err.println("Failed to make connection to the PostgreSQL server.");
            }
            /* TODO: Create tables with statistics. For example:
                words - with all words available in game, statistic of using, etc.
                users_stats - with statistic of users, like how many games played, how many words guessed, etc.
                games - with statistic of games, like how many words guessed, how many users played, etc.
                crossplayed - with statistic of crossplayed games, who had played whose games
                any other data
            */
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS history (message_id INT, user_id INT, content TEXT)").execute();
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS users (user_id INT, username VARCHAR(255), first_name VARCHAR(255), last_name VARCHAR(255))").execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
