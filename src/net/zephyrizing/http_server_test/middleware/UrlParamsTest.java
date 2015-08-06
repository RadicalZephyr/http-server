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
    public void urlParamsRegexMatches() {
        List<String> urls = Arrays.asList("file.html?abc=123",
                                          "?nofile=true",
                                          "/many?p1=1&p2=2&p3=3");

        urls.forEach(s -> {
                Matcher m = UrlParams.URL_PARAMS_RE.matcher(s);
                assertThat(m.matches(), equalTo(true));
                assertThat(m.group(1), both(notNullValue()).and(
                               anyOf(equalTo("abc=123"),
                                     equalTo("nofile=true"),
                                     equalTo("p1=1&p2=2&p3=3"))));
            });
    }

    @Test
    public void urlParamsRegexDoesntMatch() {
        List<String> urls = Arrays.asList("file.html",
                                          "",
                                          "/");

        urls.forEach(s -> {
                Matcher m = UrlParams.URL_PARAMS_RE.matcher(s);
                assertThat(m.matches(), equalTo(false));
            });
    }

    @Test
    public void canParseURLParams() {
        MockHandler mockHandler = new MockHandler();
        Handler handler = UrlParams.wrap(mockHandler);
        String key = "abc";
        String value = "123";
        String key2 = "foo";
        String value2 = "bar";
        HttpRequest request = new HttpRequest(GET, String.format("/params?%s=%s&%s=%s",
                                                                 key, value,
                                                                 key2, value2));

        assertThat(request.getUrlParam(key), nullValue());

        handler.handle(request);

        assertThat(mockHandler.calledWith, sameInstance(request));

        assertThat(request.getUrlParam(key), equalTo(value));
        assertThat(request.getUrlParam(key2), equalTo(value2));
    }
}
