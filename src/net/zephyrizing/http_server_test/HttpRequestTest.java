package net.zephyrizing.http_server_test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import net.zephyrizing.http_server.HttpProtocol;
import net.zephyrizing.http_server.HttpRequest;

import org.junit.Ignore;
import org.junit.Test;

import static java.util.Arrays.asList;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpRequestTest {

    @Test
    public void canCreateGETRequest() {
        HttpRequest request = new HttpRequest(GET, "/");

        assertEquals(GET, request.method());
        assertEquals(Paths.get("/"), request.path());
    }

    @Test
    public void canCreatePOSTRequest() {
        HttpRequest request = new HttpRequest(POST, "/root");

        assertEquals(POST, request.method());
        assertEquals(Paths.get("/root"), request.path());
    }

    @Test
    public void canResolveTheRootPath() {
        HttpRequest request = new HttpRequest(POST, "/");

        Path root = Paths.get("/root/path");

        Path requested = request.getResolvedPath(root);
        assertEquals(Paths.get("/root/path"), requested);
    }

    @Test
    public void canResolvePaths() {
        HttpRequest request = new HttpRequest(POST, "/branch");

        Path root = Paths.get("/root/path");

        Path requested = request.getResolvedPath(root);
        assertEquals(Paths.get("/root/path/branch"), requested);
    }

    @Test
    public void canAddRequestParams() {
        HttpRequest request = new HttpRequest(GET, "/");

        String key = "key";
        String value = "value";
        request.addUrlParam(key, value);

        assertThat(request.getUrlParam(key), equalTo(value));
        assertThat(request.getUrlParam("not-there"), nullValue());
    }
}
