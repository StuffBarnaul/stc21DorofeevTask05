package ru.dorofeev.homework.task05;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Server {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started!");
            while (true) {
                Socket socket = serverSocket.accept();
                try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter output = new PrintWriter(socket.getOutputStream())) {
                    StringBuilder request = new StringBuilder();
                    while (input.ready()) {
                        request.append(input.readLine());
                    }
                    System.out.println("Client connected!");
                    String requestType = request.toString().split(" ")[0];
                    if (requestType.equals("GET")) {
                        output.println("HTTP/1.1 200 OK");
                        output.println("Content-Type: text/html; charset=utf-8");
                        output.println();
                        File currentDir = new File(System.getProperty("user.dir"));
                        for (File file : Objects.requireNonNull(currentDir.listFiles())) {
                            if (file.isDirectory()) output.print("/");
                            output.print(file.getName());
                            output.println("<br>");
                        }
                        output.flush();
                    } else {
                        output.println("HTTP/1.1 404 Not Found");
                        output.println("Content-Type: text/html; charset=utf-8");
                        output.println();
                        output.flush();
                    }
                    System.out.println("Client disconnected!");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
