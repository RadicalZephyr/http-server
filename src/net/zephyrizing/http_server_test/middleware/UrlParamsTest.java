package net.zephyrizing.http_server_test.middleware;

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

        HttpRequest request = new HttpRequest(GET, "/params?abc=123");

        HttpResponse response = handler.handle(request);

        assertThat(mockHandler.calledWith, sameInstance(request));
    }
}
