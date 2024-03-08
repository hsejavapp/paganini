package ru.hse.paganini;

import ru.hse.paganini.HTTPServer;

public class Main {
    public static void main(String[] args) {
        HTTPServer server = new HTTPServer();
        server.start(8080);
    }
}
