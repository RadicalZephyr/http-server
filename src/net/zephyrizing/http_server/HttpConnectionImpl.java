package net.zephyrizing.http_server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.exceptions.HttpServerException;

public class HttpConnectionImpl implements HttpConnection {

    private Socket       socket;
    private InputStream  socketIn;
    private OutputStream socketOut;

    public HttpConnectionImpl(Socket socket, InputStream socketIn, OutputStream socketOut) {
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
    public HttpRequest getRequest() throws HttpServerException {
        return HttpProtocol.requestFromInputStream(socketIn);
    }

    @Override
    public void send(HttpResponse response) {
        try (BufferedInputStream inStream = new BufferedInputStream(HttpProtocol.responseStream(response))) {
            BufferedOutputStream outStream = new BufferedOutputStream(socketOut);
            int buffSize = 8 * 1024;
            byte[] buffer = new byte[buffSize];


            int bytesRead = inStream.read(buffer, 0, buffSize);
            while (bytesRead != -1) {
                outStream.write(buffer, 0, bytesRead);
                bytesRead = inStream.read(buffer, 0, buffSize);
            }

            outStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
