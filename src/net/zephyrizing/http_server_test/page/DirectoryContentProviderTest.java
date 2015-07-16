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
        Path contentDir = Files.createTempDirectory("http-response-test");
        Files.deleteIfExists(contentDir);

        ContentProvider provider = new DirectoryContentProvider(contentDir);
        assertThat(provider.contentExists(), equalTo(false));
        provider.getContent();
    }

    @Test
    public void testProduceContent() throws Exception {
        Path contentDir = Files.createTempDirectory("http-response-test");
        Path contentFile1 = Files.createTempFile(contentDir, "http-response-test", "");
        Path contentFile2 = Files.createTempFile(contentDir, "http-response-test", "");

        String link1 = String.format("<a href=\"/%s\">%s</a>",
                                     contentFile1.toString(),
                                     contentFile1.getFileName());
        String link2 = String.format("<a href=\"/%s\">%s</a>",
                                     contentFile2.toString(),
                                     contentFile2.getFileName());

        ContentProvider provider = new DirectoryContentProvider(contentDir);
        assertThat(provider.contentExists(), equalTo(true));
        assertThat(provider.getContent().collect(Collectors.toList()),
                   everyItem(anyOf(equalTo(link1), equalTo(link2))));
    }

}
