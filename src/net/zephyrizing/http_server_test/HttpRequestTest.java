package net.zephyrizing.http_server_test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import net.zephyrizing.http_server.HttpProtocol;
import net.zephyrizing.http_server.HttpRequest;

import org.junit.Test;

import static java.util.Arrays.asList;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

import static org.junit.Assert.*;

public class HttpRequestTest {

    @Test
    public void canCreateGETRequest() {
        HttpRequest request = new HttpRequest(GET, "/", "1.1");

        assertEquals(GET, request.method());
        assertEquals(Paths.get("/"), request.path());
        assertEquals("1.1", request.protocolVersion());
    }

    @Test
    public void canCreatePOSTRequest() {
        HttpRequest request = new HttpRequest(POST, "/root", "1.0");

        assertEquals(POST, request.method());
        assertEquals(Paths.get("/root"), request.path());
        assertEquals("1.0", request.protocolVersion());
    }

    @Test
    public void canResolveTheRootPath() {
        HttpRequest request = new HttpRequest(POST, "/", "1.0");

        Path root = Paths.get("/root/path");

        Path requested = request.getResolvedPath(root);
        assertEquals(Paths.get("/root/path"), requested);
    }

    @Test
    public void canResolvePaths() {
        HttpRequest request = new HttpRequest(POST, "/branch", "1.0");

        Path root = Paths.get("/root/path");

        Path requested = request.getResolvedPath(root);
        assertEquals(Paths.get("/root/path/branch"), requested);
    }
}
