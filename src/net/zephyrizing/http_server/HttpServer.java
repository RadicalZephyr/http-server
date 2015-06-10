package net.zephyrizing.http_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static void main(String[] args) throws Exception {
        int portNumber = Integer.parseInt(args[0]);
        System.err.println("Starting server on port " + args[0]);

        try (ServerSocket listenSocket = new ServerSocket(portNumber)) {
            System.err.println("Listening for clients...");

            try (Socket socket = listenSocket.accept();
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
                 BufferedReader stdIn = new BufferedReader(
                     new InputStreamReader(System.in))) {
                System.err.println("Connected to client. Proceeding to echo...");

                String clientInput;
                while ((clientInput = in.readLine()) != null) {
                    System.out.println("Got: "+clientInput);
                    out.println("Echo: "+clientInput);
                }
            }
        }
    }
}
