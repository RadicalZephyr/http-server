package net.zephyrizing.http_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class HttpServerSocketImpl implements HttpServerSocket {

    public ServerSocket serverSocket;

    public HttpServerSocketImpl(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void bind(int port) throws IOException {
        InetSocketAddress sockAddr = new InetSocketAddress(port);
        serverSocket.bind(sockAddr);
    }

    @Override
    public HttpRequest accept() {
        try (
            Socket socket = serverSocket.accept();
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {
            String[] requestLines = new String[] { in.readLine() };
            return new HttpRequest(Arrays.asList(requestLines));
        } catch (IOException e) {

        }
        return null;
    }

    @Override
    public HttpConnection acceptConnection() {
        try {
            Socket socket = serverSocket.accept();
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            return new HttpConnectionImpl(socket, in, out);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
