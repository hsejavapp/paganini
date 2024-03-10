package ru.hse.paganini;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HTTPServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private BufferedReader in;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (true) {
                System.out.println("Waiting for client connection...");
                clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                String request = in.readLine();
                System.out.println("Received request: " + request);

                // Парсинг запроса и обработка данных
                // В данном примере просто выводим полученные данные обратно на консоль

                String response = "HTTP/1.1 200 OK\r\n\r\nHello, client!";
                clientSocket.getOutputStream().write(response.getBytes());
                clientSocket.getOutputStream().flush();

                clientSocket.close();
                System.out.println("Client connection closed");
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

    public static void main(String[] args) {
        HTTPServer server = new HTTPServer();
        server.start(8080);
    }
}

