package net.zephyrizing.http_server_test.page;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

import net.zephyrizing.http_server.page.ContentProvider;
import net.zephyrizing.http_server.page.DirectoryContentProvider;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class DirectoryContentProviderTest {

    @Test(expected=java.lang.RuntimeException.class)
    public void testNonExistentDir() throws Exception {
        Path rootDir = Files.createTempDirectory("test-root");
        Path contentDir = Files.createTempDirectory(rootDir, "http-response-test");
        Files.deleteIfExists(contentDir);

        Path relativeDir = rootDir.relativize(contentDir);
        ContentProvider provider = new DirectoryContentProvider(rootDir, relativeDir);
        assertThat(provider.contentExists(), equalTo(false));
        provider.getContent();
    }

    @Test
    public void testPublicRoot() throws Exception {
        Path rootDir = Files.createTempDirectory("test-root");

        Path relativeDir = rootDir.relativize(rootDir);
        ContentProvider provider = new DirectoryContentProvider(rootDir, relativeDir);
        assertThat(provider.contentExists(), equalTo(true));
        assertThat(provider.getContent().collect(Collectors.toList()),
                   everyItem(anyOf(equalTo("<h1>Index of /</h1>"))));
    }

    @Test
    public void testProduceContent() throws Exception {
        Path rootDir = Files.createTempDirectory("test-root");
        Path contentDir = Files.createTempDirectory(rootDir, "http-response-test");
        Path contentFile1 = Files.createTempFile(contentDir, "http-response-test", "");
        Path contentFile2 = Files.createTempFile(contentDir, "http-response-test", "");

        String header = String.format("<h1>Index of /%s</h1>",
                                      rootDir.relativize(contentDir).getFileName());
        String format = "<a href=\"/%s\">%s</a><br>";
        String upLink = String.format(format,
                                      rootDir.relativize(contentDir.getParent()).toString(),
                                      "..");
        String link1 = String.format(format,
                                     rootDir.relativize(contentFile1).toString(),
                                     contentFile1.getFileName());
        String link2 = String.format(format,
                                     rootDir.relativize(contentFile2).toString(),
                                     contentFile2.getFileName());

        Path relativeDir = rootDir.relativize(contentDir);
        ContentProvider provider = new DirectoryContentProvider(rootDir, relativeDir);
        assertThat(provider.contentExists(), equalTo(true));
        assertThat(provider.getContent().collect(Collectors.toList()),
                   everyItem(anyOf(equalTo(header),
                                   equalTo(upLink),
                                   equalTo(link1),
                                   equalTo(link2))));
    }
}
