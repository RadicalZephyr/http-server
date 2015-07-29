package net.zephyrizing.http_server_test.requests;

import java.nio.file.Files;
import java.nio.file.Path;

import net.zephyrizing.http_server.content.ContentProvider;
import net.zephyrizing.http_server.content.FileContentProvider;

import net.zephyrizing.http_server.requests.FileSystemHandler;

import org.junit.Ignore;
import org.junit.Test;

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

}
