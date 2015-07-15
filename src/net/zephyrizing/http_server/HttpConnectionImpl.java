package net.zephyrizing.http_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.zephyrizing.http_server.HttpRequest;

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
        return HttpProtocol.requestFromLines(socketIn.lines());
    }

    @Override
    public void send(HttpResponse response) {
        HttpProtocol.responseStream(response).forEachOrdered(
            (String s) -> {
                socketOut.append(s).append("\r\n");
            });

        socketOut.flush();
    }
}
