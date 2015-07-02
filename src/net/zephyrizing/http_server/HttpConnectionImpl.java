package net.zephyrizing.http_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpProtocol;

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
        socketIn.close();
        socketOut.close();
        socket.close();
    }

    @Override
    public HttpRequest getRequest() {
        List<String> lines = new ArrayList<String>();
        String line;
        try {
            while (null != (line = socketIn.readLine())) {
                lines.add(line);
            }
            return HttpProtocol.fromLines(lines);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void send(HttpResponse response) {
        socketOut.print("HTTP/1.1 200 OK\r\n");
        socketOut.flush();
    }
}
