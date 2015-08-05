package net.zephyrizing.http_server_test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import net.zephyrizing.http_server.content.ContentProvider;
import net.zephyrizing.http_server.content.EmptyContentProvider;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpResponseTest {

    HttpResponse response;

    @Before
    public void initialize() {
        response = new HttpResponse();
    }

    @Test
    public void hasStatusCode() {
        assertThat(response.status(), equalTo(200));
    }

    @Test
    public void hasSettableStatusCode() {
        int status = 500;
        response.setStatus(status);

        assertThat(response.status(), equalTo(status));
    }

    @Test
    public void isOk() {
        response.setContent(new EmptyContentProvider());

        assertThat(response.getStatusLine(), equalTo("HTTP/1.1 200 OK"));
    }

    @Test
    public void isFourOhFour() {
        response.setStatus(404);

        assertThat(response.getStatusLine(), equalTo("HTTP/1.1 404 Not Found"));
    }

    @Test
    public void hasHeaders() {
        response.addHeader("Accept", "NOTHING");
        response.addHeader("Accept", "ToIt");
        response.addHeader("Wrong", "Things");

        assertThat(response.getHeaderStream().collect(Collectors.toList()),
                   hasItems("Accept: NOTHING,ToIt",
                            "Wrong: Things"));
    }
}
