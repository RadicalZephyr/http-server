package net.zephyrizing.http_server_test.integration;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
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

        public ServerThread() { this(null, null); }
        public ServerThread(Integer port) { this(port, null); }
        public ServerThread(String directory) { this(null, directory); }
        public ServerThread(Integer port, String directory) {
            this.port = port;
            this.directory = directory;
        }

        @Override
        public void run() {
            PrintStream oldErr = System.err;
            PrintStream newErr = new PrintStream(new ByteArrayOutputStream());
            System.setErr(newErr);
            HttpServer.main(buildOptions());
            System.setErr(oldErr);
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
    }


    @Test
    public void runDefaultServer() throws Exception {
        ServerThread server = new ServerThread();
        assertThat(Arrays.asList(server.buildOptions()), everyItem(notNullValue(String.class)));

        server.start();
        Thread.sleep(100);

        assertTrue(server.isAlive());

        server.interrupt();
    }

    @Test
    public void runServerWithPortArgument() throws Exception {
        ServerThread server = new ServerThread(10000);
        assertThat(Arrays.asList(server.buildOptions()), everyItem(notNullValue(String.class)));

        server.start();
        Thread.sleep(100);

        assertTrue(server.isAlive());

        server.interrupt();
    }

    @Test
    public void runServerWithDirectoryArgument() throws Exception {
        ServerThread server = new ServerThread(12000, "public");
        assertThat(Arrays.asList(server.buildOptions()), everyItem(notNullValue(String.class)));

        server.start();
        Thread.sleep(100);

        assertTrue(server.isAlive());

        server.interrupt();
    }

    @Test
    public void runServerAndGetResponse() throws Exception {
        int port = 14000;
        ServerThread server = new ServerThread(port, "public");
        assertThat(Arrays.asList(server.buildOptions()), everyItem(notNullValue(String.class)));

        server.start();
        Thread.sleep(100);

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

            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        }
        server.interrupt();
    }
}
