package net.zephyrizing.http_server_test;

import java.util.Arrays;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpResponseTest {

    @Test
    public void isProtocolSame() {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.1");
        HttpResponse response = HttpResponse.responseFor(request);
        assertThat(response.protocolVersion(), equalTo(request.protocolVersion()));
    }

    @Test
    public void isProtocolSame10() {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.0");
        HttpResponse response = HttpResponse.responseFor(request);
        assertThat(response.protocolVersion(), equalTo(request.protocolVersion()));
    }

}
