package net.zephyrizing.http_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
    public HttpConnection acceptConnection() {
        try {
            Socket socket = serverSocket.accept();
            InputStream  in  = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            return new HttpConnectionImpl(socket, in, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
