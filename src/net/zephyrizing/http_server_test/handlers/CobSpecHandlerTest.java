package net.zephyrizing.http_server_test.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import java.util.stream.Collectors;

import net.zephyrizing.http_server.Headers;
import net.zephyrizing.http_server.HeadersMap;
import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import static net.zephyrizing.http_server.HttpRequest.Method.*;
import net.zephyrizing.http_server.handlers.CobSpecHandler;
import net.zephyrizing.http_server.handlers.Handler;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class CobSpecHandlerTest {
    Handler handler;

    @Before
    public void initialize() {
        handler = new CobSpecHandler(Paths.get("public"));
    }

    @Test
    public void notFound() {
        HttpRequest request = new HttpRequest(GET, "/foobar");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(404));
    }

    @Test
    public void redirectPath() {
        HttpRequest request = new HttpRequest(GET, "/redirect");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(302));
        Headers headers = response.headers();
        assertThat(headers, notNullValue());
        assertThat(headers.containsKey("Location"), equalTo(true));
        assertThat(headers.get("Location"), hasItems("http://localhost:5000/"));
    }

    @Test
    public void formGetThenput() throws Exception {
        String data = "data=fatcat";
        HttpRequest request =
            new HttpRequest(PUT, "/form", null,
                            new HeadersMap(),
                            ByteBuffer.wrap(data.getBytes()));
        HttpResponse response = handler.handle(request);
        assertThat(response.status(), equalTo(200));

        request = new HttpRequest(GET, "/form");
        response = handler.handle(request);
        assertThat(response.status(), equalTo(200));
        BufferedReader r = new BufferedReader(
            new InputStreamReader(response.getData()));
        String bodyContent = r.readLine();
        assertThat(bodyContent, equalTo(data));
    }

    @Test
    public void simplePut() {
        HttpRequest request = new HttpRequest(PUT, "/form");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
    }

    @Test
    public void simplePost() {
        HttpRequest request = new HttpRequest(POST, "/form");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
    }

    @Test
    public void simpleOptions() {
        HttpRequest request = new HttpRequest(OPTIONS, "/method_options");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
        Headers headers = response.headers();
        assertThat(headers, notNullValue());
        assertThat(headers.containsKey("Allow"), equalTo(true));
        assertThat(headers.get("Allow"), everyItem(anyOf(equalTo("GET"),
                                                         equalTo("HEAD"),
                                                         equalTo("POST"),
                                                         equalTo("OPTIONS"),
                                                         equalTo("PUT"))));
    }

    @Test
    public void parameterDecode() {
        String cobSpecParams = "variable_1=Operators%20%3C%2C%20%3E%2C%20%3D"+
            "%2C%20!%3D%3B%20%2B%2C%20-%2C%20*%2C%20%26%2C%20%40%2C%20%23%2C"+
            "%20%24%2C%20%5B%2C%20%5D%3A%20%22is%20that%20all%22%3F&variable_2=stuff";
        HttpRequest request = new HttpRequest(GET, "/parameters", cobSpecParams,
                                              null, null);
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
        BufferedReader r = new BufferedReader(
            new InputStreamReader(response.getData()));
        List<String> lines = r.lines().collect(Collectors.toList());

        assertThat(lines, hasItems("variable_2 = stuff",
                                   "variable_1 = Operators <, >, =, !=; +, -, *, &, @, #, $, [, ]: \"is that all\"?"));
    }

    @Test
    public void getPostGetdeleteGet() throws IOException {
        HttpRequest request;
        HttpResponse response;
        BufferedReader r;

        // Initial GET
        request = new HttpRequest(GET, "/form");
        response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
        r =
            new BufferedReader(
                new InputStreamReader(response.getData()));
        assertThat(r.readLine(), nullValue());

        /// POST content
        Headers headers = new HeadersMap();
        String body = "stuffnthings";
        String bodyLength = Integer.toString(body.length());
        headers.put("Content-Length", Arrays.asList(bodyLength));
        ByteBuffer bodyBytes = ByteBuffer.wrap(body.getBytes());
        request = new HttpRequest(POST, "/form", null,
                                  headers, bodyBytes);
        response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));

        /// Intermediate GET
        request = new HttpRequest(GET, "/form");
        response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
        r = new BufferedReader(
            new InputStreamReader(response.getData()));
        assertThat(r.readLine(), equalTo(body));

        /// DELETE
        request = new HttpRequest(DELETE, "/form");
        response = handler.handle(request);

        /// Final GET
        request = new HttpRequest(GET, "/form");
        response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
        r =
            new BufferedReader(
                new InputStreamReader(response.getData()));
        assertThat(r.readLine(), nullValue());
    }
}
