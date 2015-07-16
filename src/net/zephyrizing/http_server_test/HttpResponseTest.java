package net.zephyrizing.http_server_test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpResponseTest {

    HttpRequest  request;
    HttpResponse response;

    @Before
    public void initialize() {
        request = new HttpRequest(Method.GET, "/", "1.1");
        response = HttpResponse.responseFor(request);
    }

    @Test
    public void isProtocolSame() {
        assertThat(response.protocolVersion(), equalTo(request.protocolVersion()));
    }

    @Test
    public void isProtocolSame10() {
        assertThat(response.protocolVersion(), equalTo(request.protocolVersion()));
    }
}
