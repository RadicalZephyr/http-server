package net.zephyrizing.http_server_test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;

import net.zephyrizing.http_server.HttpConnection;
import net.zephyrizing.http_server.HttpConnectionImpl;
import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static java.util.Arrays.asList;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpConnectionTest {

    String[] requestStrings = new String[] { "GET / HTTP/1.1",
                                             "" };

    Socket         socket;
    BufferedReader socketIn;
    PrintWriter    socketOut;

    @Before
    public void initialize() {
        socket    = new Socket();
        socketIn  = new BufferedReader(new StringReader(String.join("\r\n", asList(requestStrings))));
        socketOut = new PrintWriter(new ByteArrayOutputStream());
    }

    @Test
    public void constructsARequest() {
        HttpConnection connection = new HttpConnectionImpl(socket, socketIn, socketOut);
        HttpRequest request = connection.getRequest();
        assertThat(request.method(), equalTo(GET));
    }

    @Test
    public void sendsAResponse() {
        HttpConnection connection = new HttpConnectionImpl(socket, socketIn, socketOut);
        HttpRequest request = connection.getRequest();
        HttpResponse response = HttpResponse.responseFor(request);
        connection.send(response);
    }
}
