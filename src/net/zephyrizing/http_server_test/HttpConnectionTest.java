package net.zephyrizing.http_server_test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.Arrays;

import net.zephyrizing.http_server.HttpConnection;
import net.zephyrizing.http_server.HttpConnectionImpl;
import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.content.ContentProvider;
import net.zephyrizing.http_server.content.EmptyContentProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static java.util.Arrays.asList;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpConnectionTest {

    String[] requestStrings = new String[] { "GET / HTTP/1.1",
                                             "\r\n" };

    Socket                socket;
    InputStream           socketIn;
    ByteArrayOutputStream socketOut;

    @Before
    public void initialize() {
        socket    = new Socket();
        socketIn  = new ByteArrayInputStream(String.join("\r\n", asList(requestStrings)).getBytes());

        socketOut = new ByteArrayOutputStream();
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
        HttpResponse response = new HttpResponse();
        response.setContent(new EmptyContentProvider());
        connection.send(response);

        assertThat(socketOut.toString(), equalTo("HTTP/1.1 200 OK\r\n\r\n"));
    }
}
