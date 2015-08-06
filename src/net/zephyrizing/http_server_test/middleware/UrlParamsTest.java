package net.zephyrizing.http_server_test.middleware;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.handlers.Handler;
import net.zephyrizing.http_server.middleware.UrlParams;

import org.junit.Ignore;
import org.junit.Test;

import static net.zephyrizing.http_server.HttpRequest.Method.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class UrlParamsTest {

    class MockHandler implements Handler {
        public HttpRequest calledWith;

        @Override
        public HttpResponse handle(HttpRequest request) {
            this.calledWith = request;
            return null;
        }
    }

    @Test
    public void canParseURLParams() {
        MockHandler mockHandler = new MockHandler();
        Handler handler = UrlParams.wrap(mockHandler);
        String key = "abc";
        String value = "123";
        String key2 = "foo";
        String value2 = "bar";
        HttpRequest request = new HttpRequest(GET, "/params",
                                              String.format("%s=%s&%s=%s",
                                                            key, value,
                                                            key2, value2),
                                              null, null);

        assertThat(request.getUrlParam(key), nullValue());

        handler.handle(request);

        assertThat(mockHandler.calledWith, sameInstance(request));

        assertThat(request.getUrlParam(key), equalTo(value));
        assertThat(request.getUrlParam(key2), equalTo(value2));
    }
}
