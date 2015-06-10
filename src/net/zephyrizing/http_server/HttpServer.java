package net.zephyrizing.http_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    public static void main(String[] args) throws Exception {
        int portNumber;
        if (args.length == 1) {
         portNumber = Integer.parseInt(args[0]);
        } else {
            portNumber = 80;
        }
        System.err.println("Starting server on port " + portNumber);

        try (ServerSocket listenSocket = new ServerSocket(portNumber)) {
            System.err.println("Listening for clients...");

            try (Socket socket = listenSocket.accept();
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
                 BufferedReader stdIn = new BufferedReader(
                     new InputStreamReader(System.in))) {
                System.err.println("Connected to client.");

                String request = in.readLine();
                String[] params = request.split(" ");

                assert(params.length == 3);

                String method = params[0];
                String path = params[1];
                String protocolVersion = params[2];

                System.out.format("Client requested to %s file %s over %s.\n",
                                  method, path, protocolVersion);
                out.format("%s 200 OK\r\n", protocolVersion);
            }
        }
    }
}
