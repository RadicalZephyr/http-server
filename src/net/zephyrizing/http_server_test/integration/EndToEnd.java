package net.zephyrizing.http_server_test.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import net.zephyrizing.http_server.HttpServer;

import org.junit.Test;

import static org.junit.Assert.*;

public class EndToEnd {

    @Test
    public void canStartTheServer() {
        PrintStream stringOut = new PrintStream(new ByteArrayOutputStream());
        PrintStream oldErr = System.err;
        System.setErr(stringOut);
        HttpServer.main(new String[] {"9000"});
        System.setErr(oldErr);
    }
}
