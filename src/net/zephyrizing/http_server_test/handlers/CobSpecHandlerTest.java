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
        HttpRequest request = new HttpRequest(GET, "/foobar", "1.1");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(404));
    }

    @Test
    public void redirectPath() {
        HttpRequest request = new HttpRequest(GET, "/redirect", "1.1");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(302));
        Map<String, List<String>> headers = response.headers();
        assertThat(headers, notNullValue());
        assertThat(headers.containsKey("Location"), equalTo(true));
        assertThat(headers.get("Location"), hasItems("http://localhost:5000/"));
    }

    @Test
    public void formGetThenput() throws Exception {
        String data = "data=fatcat";
        HttpRequest request =
            new HttpRequest(PUT, "/form", "1.1",
                            new HashMap<String, List<String>>(),
                            ByteBuffer.wrap(data.getBytes()));
        HttpResponse response = handler.handle(request);
        assertThat(response.status(), equalTo(200));

        request = new HttpRequest(GET, "/form", "1.1");
        response = handler.handle(request);
        assertThat(response.status(), equalTo(200));
        BufferedReader r = new BufferedReader(
            new InputStreamReader(response.getData()));
        String bodyContent = r.readLine();
        assertThat(bodyContent, equalTo(data));
    }

    @Test
    public void simplePut() {
        HttpRequest request = new HttpRequest(PUT, "/form", "1.1");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
    }

    @Test
    public void simplePost() {
        HttpRequest request = new HttpRequest(POST, "/form", "1.1");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
    }

    @Test
    public void simpleOptions() {
        HttpRequest request = new HttpRequest(OPTIONS, "/method_options", "1.1");
        HttpResponse response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
        Map<String, List<String>> headers = response.headers();
        assertThat(headers, notNullValue());
        assertThat(headers.containsKey("Allow"), equalTo(true));
        assertThat(headers.get("Allow"), everyItem(anyOf(equalTo("GET"),
                                                         equalTo("HEAD"),
                                                         equalTo("POST"),
                                                         equalTo("OPTIONS"),
                                                         equalTo("PUT"))));
    }

    @Test
    public void getPostGetdeleteGet() throws IOException {
        HttpRequest request;
        HttpResponse response;
        BufferedReader r;

        // Initial GET
        request = new HttpRequest(GET, "/form", "1.1");
        response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
        r =
            new BufferedReader(
                new InputStreamReader(response.getData()));
        assertThat(r.readLine(), nullValue());

        /// POST content
        Map<String, List<String>> headers = new HashMap<String, List<String>>();
        String body = "stuffnthings";
        String bodyLength = Integer.toString(body.length());
        headers.put("Content-Length", Arrays.asList(bodyLength));
        ByteBuffer bodyBytes = ByteBuffer.wrap(body.getBytes());
        request = new HttpRequest(POST, "/form", "1.1",
                                  headers, bodyBytes);
        response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));

        /// Intermediate GET
        request = new HttpRequest(GET, "/form", "1.1");
        response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
        r = new BufferedReader(
            new InputStreamReader(response.getData()));
        assertThat(r.readLine(), equalTo(body));

        /// DELETE
        request = new HttpRequest(DELETE, "/form", "1.1");
        response = handler.handle(request);

        /// Final GET
        request = new HttpRequest(GET, "/form", "1.1");
        response = handler.handle(request);

        assertThat(response, notNullValue());
        assertThat(response.status(), equalTo(200));
        r =
            new BufferedReader(
                new InputStreamReader(response.getData()));
        assertThat(r.readLine(), nullValue());
    }
}
