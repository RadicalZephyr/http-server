package net.zephyrizing.http_server_test.page;

import java.nio.file.Files;
import java.nio.file.Path;
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

    @Test
    public void testProduceContent() throws Exception {
        List<String> content = Arrays.asList(new String[] {"abc", "def", "ghi"});
        Path contentFile = Files.createTempFile("http-response-test", "");
        Files.write(contentFile, content);

        ContentProvider provider = new FileContentProvider(contentFile);
        assertThat(provider.getContent().collect(Collectors.toList()),
                   equalTo(content));
    }
}
