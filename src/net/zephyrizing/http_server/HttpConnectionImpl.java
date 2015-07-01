package net.zephyrizing.http_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpConnectionImpl implements HttpConnection {

    private Socket         socket;
    private BufferedReader socketIn;
    private PrintWriter    socketOut;

    public HttpConnectionImpl(Socket socket, BufferedReader socketIn, PrintWriter socketOut) {
        this.socket = socket;
        this.socketIn = socketIn;
        this.socketOut = socketOut;
    }

    @Override
    public void close() throws IOException {
        socket.close();
        socketIn.close();
        socketOut.close();
    }
}
