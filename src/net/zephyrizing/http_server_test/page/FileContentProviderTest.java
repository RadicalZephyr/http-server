package net.zephyrizing.http_server_test.page;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.zephyrizing.http_server.page.ContentProvider;
import net.zephyrizing.http_server.page.FileContentProvider;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class FileContentProviderTest {

    @Test(expected=java.lang.RuntimeException.class)
    public void testNonExistentFile() throws Exception {
        Path contentFile = Files.createTempFile("http-response-test", "");
        Files.deleteIfExists(contentFile);

        ContentProvider provider = new FileContentProvider(contentFile);
        assertThat(provider.contentExists(), equalTo(false));
        provider.getContent();
    }

    @Test
    public void testProduceContent() throws Exception {
        List<String> content = Arrays.asList(new String[] {"abc", "def", "ghi"});
        Path contentFile = Files.createTempFile("http-response-test", "");
        Files.write(contentFile, content);

        ContentProvider provider = new FileContentProvider(contentFile);
        BufferedReader reader  = new BufferedReader(new InputStreamReader(provider.getContent()));

        assertThat(reader.lines().collect(Collectors.toList()),
                   equalTo(content));

        Files.delete(contentFile);
    }

    @Test
    public void testProduceBinaryContent() throws Exception {
        Path imageFile = Paths.get("resources/flyingcat.jpeg");

        assertThat(Files.exists(imageFile), equalTo(true));
        assertThat(Files.isRegularFile(imageFile), equalTo(true));

        ContentProvider provider = new FileContentProvider(imageFile);

        assertThat(provider.contentExists(), equalTo(true));

        Path tempOutput = Files.createTempFile("http-response-test-", "-image");
        Files.delete(tempOutput);
        assertThat(Files.exists(tempOutput), equalTo(false));

        Files.copy(provider.getContent(), tempOutput);

        assertThat(Files.size(tempOutput), equalTo(Files.size(imageFile)));
        assertThat(Files.readAllBytes(tempOutput), equalTo(Files.readAllBytes(imageFile)));

        Files.delete(tempOutput);
    }
}
