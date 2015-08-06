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

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpRequestTest {

    @Test
    public void canCreateGETRequest() {
        HttpRequest request = new HttpRequest(GET, "/");

        assertThat(request.method(), equalTo(GET));
        assertThat(request.path(), equalTo(Paths.get("/")));
    }

    @Test
    public void canCreatePOSTRequest() {
        HttpRequest request = new HttpRequest(POST, "/root");

        assertThat(request.method(), equalTo(POST));
        assertThat(request.path(), equalTo(Paths.get("/root")));
    }

    @Test
    public void canResolveTheRootPath() {
        HttpRequest request = new HttpRequest(POST, "/");

        Path root = Paths.get("/root/path");

        Path requested = request.getResolvedPath(root);
        assertThat(requested, equalTo(Paths.get("/root/path")));
    }

    @Test
    public void canResolvePaths() {
        HttpRequest request = new HttpRequest(POST, "/branch");

        Path root = Paths.get("/root/path");

        Path requested = request.getResolvedPath(root);
        assertThat(requested, equalTo(Paths.get("/root/path/branch")));
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
