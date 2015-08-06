package net.zephyrizing.http_server_test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.RequestBuilder;
import static net.zephyrizing.http_server.HttpRequest.Method.*;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class RequestBuilderTest {

    @Test
    public void canAddAMethodAndPath() {
        Method m = GET;
        String p = "/";

        RequestBuilder b = new RequestBuilder()
            .method(m)
            .path(p);

        HttpRequest request = b.build();

        assertThat(request.method(),          equalTo(m));
        assertThat(request.path().toString(), equalTo(p));
    }

    @Test
    public void canAddHeaders() {
        RequestBuilder b = baseRequestBuilder();

        String key = "Content-Length";
        List<String> val = Arrays.asList("A", "B");
        b.header(key, val);

        HttpRequest request = b.build();

        assertThat(request, notNullValue());

        Map<String, List<String>> headers = request.headers();
        assertThat(headers, notNullValue());
        assertThat(headers.keySet(), hasItem(key));
        assertThat(headers.get(key), equalTo(val));
    }

    @Test
    public void canReturnContentLength() {
        RequestBuilder b = baseRequestBuilder();

        String key = "Content-Length";
        List<String> val = Arrays.asList("10");
        b.header(key, val);

        assertThat(b.hasContentHeader(), equalTo(true));
        assertThat(b.contentLength(), equalTo(10L));
    }

    @Test
    public void canAddBody() {
        RequestBuilder rb = baseRequestBuilder();

        ByteBuffer bb = ByteBuffer.allocate(10);
        rb.body(bb);

        HttpRequest request = rb.build();

        assertThat(request.body(), sameInstance(bb));
    }

    private RequestBuilder baseRequestBuilder() {
        return new RequestBuilder()
            .method(GET)
            .path("/");
    }
}
