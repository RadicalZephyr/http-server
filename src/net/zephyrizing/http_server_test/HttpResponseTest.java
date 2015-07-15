package net.zephyrizing.http_server_test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpResponseTest {

    @Test
    public void isProtocolSame() {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.1");
        HttpResponse response = HttpResponse.responseFor(request);
        assertThat(response.protocolVersion(), equalTo(request.protocolVersion()));
    }

    @Test
    public void isProtocolSame10() {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.0");
        HttpResponse response = HttpResponse.responseFor(request);
        assertThat(response.protocolVersion(), equalTo(request.protocolVersion()));
    }

    @Test
    public void hasFileContent() throws Exception {
        HttpRequest request = new HttpRequest(Method.GET, "/", "1.0");
        HttpResponse response = HttpResponse.responseFor(request);

        List<String> content = Arrays.asList(new String[] {"abc", "def", "ghi"});
        Path contentFile = Files.createTempFile("http-response-",
                                            "-test");
        Files.write(contentFile, content);

        response.setContent(contentFile);
        assertThat(response.getDataStream().collect(Collectors.toList()),
                   equalTo(content));
    }
}
