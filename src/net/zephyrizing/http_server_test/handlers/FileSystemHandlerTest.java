package net.zephyrizing.http_server_test.handlers;

import java.nio.file.Files;
import java.nio.file.Path;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.content.ContentProvider;
import net.zephyrizing.http_server.content.FileContentProvider;

import net.zephyrizing.http_server.handlers.FileSystemHandler;

import org.junit.Ignore;
import org.junit.Test;

import static net.zephyrizing.http_server.HttpRequest.Method.*;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class FileSystemHandlerTest {

    @Test
    public void testProducesFiles() throws Exception {
        Path rootDir = Files.createTempDirectory("test-root");
        Path contentDir = Files.createTempDirectory(rootDir, "http-response-test");

        FileSystemHandler handler = new FileSystemHandler(rootDir);

        Path contentFile = Files.createTempFile(contentDir, "http-response-test", "");
        ContentProvider provider = handler.getContentFor(rootDir.relativize(contentFile));

        assertThat(provider, is(not(equalTo(null))));
        assertThat(provider.contentExists(), equalTo(true));
    }

    @Test
    public void testProducesDirectories() throws Exception {
        Path rootDir = Files.createTempDirectory("test-root");
        Path contentDir = Files.createTempDirectory(rootDir, "http-response-test");

        FileSystemHandler handler = new FileSystemHandler(rootDir);

        Path contentFile = Files.createTempFile(contentDir, "http-response-test", "");
        ContentProvider provider = handler.getContentFor(rootDir.relativize(contentDir));

        assertThat(provider, is(not(equalTo(null))));
        assertThat(provider.contentExists(), equalTo(true));
    }

    @Test
    public void testReportsOptionsAsOnlyGET() throws Exception {
        Path rootDir = Files.createTempDirectory("test-root");
        Path contentDir = Files.createTempDirectory(rootDir, "http-response-test");

        FileSystemHandler handler = new FileSystemHandler(rootDir);

        HttpRequest request = new HttpRequest(OPTIONS, "/");
        HttpResponse response = handler.handle(request);

        assertThat(response.headers().get("Allow"), containsString("GET"));
    }

    @Test
    public void testReportsMethodNotAllowed() throws Exception {
        Path rootDir = Files.createTempDirectory("test-root");
        Path contentDir = Files.createTempDirectory(rootDir, "http-response-test");

        FileSystemHandler handler = new FileSystemHandler(rootDir);

        HttpRequest request = new HttpRequest(POST, "/things");
        HttpResponse response = handler.handle(request);

        assertThat(response.status(), equalTo(405));
    }
}
