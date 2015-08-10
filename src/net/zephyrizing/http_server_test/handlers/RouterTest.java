package net.zephyrizing.http_server_test.handlers;

import java.nio.file.Paths;

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
        HttpRequest request = new HttpRequest(GET, "/");
        final HttpResponse response = new HttpResponse();
        router.addHandler(GET, "/", (HttpRequest iRequest) -> {
                return response;
            });

        assertThat(router.handle(request), equalTo(response));
    }

    @Test
    public void canAddMultipleRoutes() throws Exception {
        HttpRequest request = new HttpRequest(GET, "/root-beer");
        final HttpResponse response = new HttpResponse();
        router.addHandler(GET, "/root-beer", (HttpRequest iRequest) -> {
                return response;
            });

        HttpRequest request2 = new HttpRequest(GET, "/");
        final HttpResponse response2 = new HttpResponse();
        router.addHandler(GET, "/", (HttpRequest iRequest) -> {
                return response2;
            });

        assertThat(router.handle(request), equalTo(response));
        assertThat(router.handle(request2), equalTo(response2));
    }

    @Test
    public void canDifferentiateBetweenMethods() throws Exception {
        HttpRequest request = new HttpRequest(POST, "/");
        final HttpResponse response = new HttpResponse();
        router.addHandler(POST, "/", (HttpRequest iRequest) -> {
                return response;
            });

        HttpRequest request2 = new HttpRequest(GET, "/");
        final HttpResponse response2 = new HttpResponse();
        router.addHandler(GET, "/", (HttpRequest iRequest) -> {
                return response2;
            });

        assertThat(router.handle(request), equalTo(response));
        assertThat(router.handle(request2), equalTo(response2));

    }

    @Test
    public void failsGracefullyWithNoMatch() {
        HttpRequest request = new HttpRequest(POST, "/");
        assertThat(router.handle(request), nullValue());
    }

    @Test
    public void canGenerateOptionsForRoute() {
        router.addHandler(GET, "/", (HttpRequest iRequest) -> null);
        router.addHandler(PUT, "/", (HttpRequest iRequest) -> null);

        HttpRequest request = new HttpRequest(OPTIONS, "/");
        HttpResponse response = router.handle(request);
        assertThat(response.headers().get("Allow"), both(containsString("GET"))
                   .and(containsString("PUT")));
    }
}
