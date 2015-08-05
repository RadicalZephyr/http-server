package net.zephyrizing.http_server_test.handlers;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import static net.zephyrizing.http_server.HttpRequest.Method.*;

import net.zephyrizing.http_server.handlers.Handler;
import net.zephyrizing.http_server.handlers.NullHandler;
import net.zephyrizing.http_server.handlers.SequentialHandler;

public class SequentialHandlerTest {

    @Test
    public void returnsNullWithNoHandlers() throws Exception {
        Handler handler = new SequentialHandler();
        HttpRequest request = new HttpRequest(GET, "/", "1.1");

        assertThat(handler.handle(request), nullValue());
    }

    @Test
    public void returnsARequestGivenAHandler() throws Exception {
        Handler handler = new SequentialHandler(new NullHandler());
        HttpRequest request = new HttpRequest(GET, "/", "1.1");

        assertThat(handler.handle(request), not(nullValue()));
    }


    @Test
    public void skipsTheFirstHandlerWhenNullResult() throws Exception {
        final HttpResponse response = new HttpResponse();
        Handler handler = new SequentialHandler((HttpRequest r) -> null,
                                                (HttpRequest r) -> response);
        HttpRequest request = new HttpRequest(GET, "/", "1.1");

        assertThat(handler.handle(request), equalTo(response));
    }
}
