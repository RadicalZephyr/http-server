package net.zephyrizing.http_server_test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;

import net.zephyrizing.http_server.HttpInputStream;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpInputStreamTest {

    @Test
    public void canReadNetworkLines() throws Exception {
        String content = "hello\r\nworld\r\n";
        HttpInputStream s =
            new HttpInputStream(
                5,
                new BufferedInputStream(
                    new ByteArrayInputStream(content.getBytes())));

        assertThat(s.readLine(), equalTo("hello"));
        assertThat(s.readLine(), equalTo("world"));
    }
}
