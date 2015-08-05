package net.zephyrizing.http_server_test.integration;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.zephyrizing.http_server.HttpServer;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class SmokeTest {
    public class ServerThread extends Thread {
        Integer port;
        String directory;
        private boolean withOutput = false;
        private PrintStream oldErr = System.err;

        public ServerThread(String name) { this(name, null, null); }
        public ServerThread(String name, Integer port) { this(name, port, null); }
        public ServerThread(String name, String directory) { this(name, null, directory); }
        public ServerThread(String name, Integer port, String directory) {
            this.setName(name);
            this.port = port;
            this.directory = directory;
        }

        public void print(String message) {
            System.err.format("%d-%s: %s\n", getId(), getName(), message);
        }

        @Override
        public void run() {

            try {
                HttpServer.main(buildOptions());
            } catch (InterruptedException e) {
            }
        }

        public String[] buildOptions() {
            List<String> opts = new ArrayList<String>();
            int i = 0;
            if (port != null) {
                opts.add("-p");
                opts.add(port.toString());
            }
            if (directory != null) {
                opts.add("-d");
                opts.add(directory);
            }
            return opts.toArray(new String[0]);
        }

        private int port() {
            return (this.port == null) ? 5000 : this.port;
        }

        private void pingServer(int port) {
            try (Socket s = new Socket(InetAddress.getLocalHost(), port);
                 PrintWriter out = new PrintWriter(s.getOutputStream())) {
                out.append("\r\n").append("\r\n").append("\r\n");
            } catch (IOException e) {
            }
        }

        public void startServer() {
            if (!withOutput) {
                PrintStream newErr = new PrintStream(new ByteArrayOutputStream());
                System.setErr(newErr);
            }
            try {
                while(!available(port())) {
                    Thread.sleep(10);
                    print("waiting to start server");
                }
                this.start();
                while(available(port())) {
                    Thread.sleep(10);
                    print("waiting for server to start");
                }
            } catch (InterruptedException e) {
            }
            print("server started");
        }

        public void shutdownServer() {
            try {
                this.interrupt();
                pingServer(port());
                try {
                    print("waiting for server thread to quit");
                    this.join();
                    print("joined server thread.");
                } catch (InterruptedException e) {
                }
            } finally {
                if(!withOutput) {
                    System.setErr(oldErr);
                }
            }
        }
    }


    @Test
    public void runDefaultServer() throws Exception {
        ServerThread server = new ServerThread("default");
        assertThat(Arrays.asList(server.buildOptions()), everyItem(notNullValue(String.class)));

        server.startServer();

        assertTrue(server.isAlive());

        server.shutdownServer();
    }

    @Test
    public void runServerWithPortArgument() throws Exception {
        int port = 10000;
        ServerThread server = new ServerThread("withPort", port);
        assertThat(Arrays.asList(server.buildOptions()), everyItem(notNullValue(String.class)));

        server.startServer();

        assertTrue(server.isAlive());

        server.shutdownServer();
    }

    @Test
    public void runServerWithDirectoryArgument() throws Exception {
        int port = 10000;
        ServerThread server = new ServerThread("withPortAndDir", port, "public");
        assertThat(Arrays.asList(server.buildOptions()), everyItem(notNullValue(String.class)));

        server.startServer();

        assertTrue(server.isAlive());

        server.shutdownServer();
    }

    @Test
    public void runServerAndGetResponse() throws Exception {
        int port = 10000;
        ServerThread server = new ServerThread("getResponse", port, "public");
        assertThat(Arrays.asList(server.buildOptions()), everyItem(notNullValue(String.class)));

        server.startServer();

        try (Socket socket = new Socket("localhost", port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                 new InputStreamReader(socket.getInputStream()));) {
            out.print("GET /.gitkeep HTTP/1.1\r\n");
            out.print("Host: localhost\r\n");
            out.print("\r\n");
            out.flush();

            String line = in.readLine();
            assertThat(line, equalTo("HTTP/1.1 200 OK"));

            line = in.readLine();
            assertThat(line, equalTo(""));

            line = in.readLine();
            assertThat(line, equalTo(null));
        } finally {
            server.shutdownServer();
        }
    }

    // From Apache Camel
    public boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }
}
