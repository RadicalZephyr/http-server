package net.zephyrizing.http_server_test.handlers;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import static net.zephyrizing.http_server.HttpRequest.Method.*;

import net.zephyrizing.http_server.handlers.Router;

public class RouterTest {

    private Router router;

    @Before
    public void initialize() {
        router = new Router();
    }

    @Test
    public void canAddARoute() throws Exception {
        HttpRequest request = new HttpRequest(GET, "/", "1.1");
        final HttpResponse response = HttpResponse.responseFor(request);
        router.addHandler(GET, "/", (HttpRequest iRequest) -> {
                return response;
            });

        assertThat(router.handle(request), equalTo(response));
    }

    @Test
    public void canAddMultipleRoutes() throws Exception {
        HttpRequest request = new HttpRequest(GET, "/root-beer", "1.1");
        final HttpResponse response = HttpResponse.responseFor(request);
        router.addHandler(GET, "/root-beer", (HttpRequest iRequest) -> {
                return response;
            });

        HttpRequest request2 = new HttpRequest(GET, "/", "1.1");
        final HttpResponse response2 = HttpResponse.responseFor(request2);
        router.addHandler(GET, "/", (HttpRequest iRequest) -> {
                return response2;
            });

        assertThat(router.handle(request), equalTo(response));
        assertThat(router.handle(request2), equalTo(response2));
    }

}
