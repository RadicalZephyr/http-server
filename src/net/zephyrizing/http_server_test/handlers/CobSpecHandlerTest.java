package net.zephyrizing.http_server_test.handlers;

import java.nio.file.Paths;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import static net.zephyrizing.http_server.HttpRequest.Method.*;

import net.zephyrizing.http_server.handlers.Handler;
import net.zephyrizing.http_server.handlers.CobSpecHandler;

public class CobSpecHandlerTest {
    Handler handler;

    @Before
    public void initialize() {
        handler = new CobSpecHandler(Paths.get("public"));
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
}
