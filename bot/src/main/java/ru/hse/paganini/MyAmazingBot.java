package ru.hse.paganini;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import ru.hse.paganini.database.PostgreSQL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class MyAmazingBot extends TelegramLongPollingBot {
    // TODO: почистить код, сделать его более читаемым
    // TODO: Часть кода в onUpdateReceived можно вынести в отдельные методы
    // TODO: Добавить логирование

    PostgreSQL postgresql;
    Connection conn;

    public MyAmazingBot() {
        this(new DefaultBotOptions());
    }

    public MyAmazingBot(DefaultBotOptions options) {
        super(options);
        System.out.println("MyAmazingBot init");

        this.postgresql = getPostgreSQL();
        this.conn = this.postgresql.getConnection();
    }

    @Override
    public String getBotUsername() {
        return ConfigParser.getBotParameter("resources/telegram.yaml", "name");
    }

    @Override
    public String getBotToken() {
        return ConfigParser.getBotParameter("resources/telegram.yaml", "token");
    }

    @Override
    public void onUpdateReceived(Update update) {
        String text = update.getMessage().getText();
        String user_id = update.getMessage().getChat().getId().toString();
        if (text.equals("/stats")) {
            try {
                ResultSet results = this.conn.prepareStatement("SELECT * FROM users WHERE user_id == '" + user_id + "'").executeQuery();
                results.getArray("messages");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (text.equals("/clear")) {
            try {
                this.conn.prepareStatement("DELETE FROM users").execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            String username = update.getMessage().getChat().getUserName();
            String message_id = update.getMessage().getMessageId().toString();
            try {
                this.conn
                .prepareStatement("INSERT INTO history (message_id, user_id, content) values ('" + message_id + "', '" + user_id + "', '" + text + "')")
                .execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // TODO: here we should call methods for playing wordly game.
            SendMessage message = new SendMessage();
            message.setChatId(update.getMessage().getChatId());
            message.setText("You said: " + text);
            try {
                execute(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // тут переделать на работу с нашим бэком
    private PostgreSQL getPostgreSQL() {
        return new PostgreSQL();
    }
}
