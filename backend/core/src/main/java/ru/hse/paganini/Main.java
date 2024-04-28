package ru.hse.paganini;

public class Main {
    public static void main(String[] args) {
        HTTPServer server = new HTTPServer();
        server.start(8080);
    }
}
