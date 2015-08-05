package net.zephyrizing.http_server_test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import net.zephyrizing.http_server.HttpInputStream;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpInputStreamTest {

    @Test(expected=IOException.class)
    public void throwsWhenNoNewlineAvailable() throws Exception {
        String content = "hello";
        HttpInputStream s = httpInputStream(5, content);

        s.readLine();
    }

    @Test
    public void canReadNetworkLines() throws Exception {
        String content = "hello\r\nworld\r\n";
        HttpInputStream s = httpInputStream(5, content);

        assertThat(s.readLine(), equalTo("hello"));
        assertThat(s.readLine(), equalTo("world"));
    }

    @Test
    public void canReadWithSingleCharBuffer() throws Exception {
        String content = "hello\r\nworld\r\n";
        HttpInputStream s = httpInputStream(1, content);

        assertThat(s.readLine(), equalTo("hello"));
        assertThat(s.readLine(), equalTo("world"));
    }

    @Test
    public void canReadVaryingLengthLines() throws Exception {
        String content = "short\r\ntoolong\r\n";
        HttpInputStream s = httpInputStream(7, content);

        assertThat(s.readLine(), equalTo("short"));
        assertThat(s.readLine(), equalTo("toolong"));
    }

    @Test
    public void canReadLinesTwiceAsBigAsBuffer() throws Exception {
        String content = "muchtoolong!\r\n";
        HttpInputStream s = httpInputStream(7, content);

        assertThat(s.readLine(), equalTo("muchtoolong!"));
    }

    @Test
    public void silentlyEatsAnInitialBlankLine() throws Exception {
        String content = "\r\nline1\r\n\r\n";
        HttpInputStream s = httpInputStream(2, content);

        assertThat(s.readLine(), equalTo("line1"));
        assertThat(s.readLine(), nullValue());
    }

    @Test
    public void returnsNullOnEmptyLineRead() throws Exception {
        String content = "line1\r\nline2\r\nline3\r\n\r\n";
        HttpInputStream s = httpInputStream(12, content);

        assertThat(s.readLine(), equalTo("line1"));
        assertThat(s.readLine(), equalTo("line2"));
        assertThat(s.readLine(), equalTo("line3"));
        assertThat(s.readLine(), nullValue());
    }

    @Test
    public void canReadCRAndLFInSeparateBuffers() throws Exception {
        String content = "line\r\notherstuff\r\n";
        HttpInputStream s = httpInputStream(5, content);

        assertThat(s.readLine(), equalTo("line"));
        assertThat(s.readLine(), equalTo("otherstuff"));
    }

    @Test
    public void stopsReadingByLinesAfterBlankLine() throws Exception {
        String content = "line\r\n\r\notherthings";
        HttpInputStream s = httpInputStream(12, content);

        assertThat(s.readLine(), equalTo("line"));
        assertThat(s.readLine(), nullValue());
        assertThat(s.readLine(), nullValue());
        assertThat(s.readLine(), nullValue());

        String bodyContent = "otherthings";
        ByteBuffer bodyBytes = ByteBuffer.wrap(bodyContent.getBytes());
        assertThat(s.readBody(bodyContent.length()), equalTo(bodyBytes));
    }

    @Test
    public void cantReadBodyUntilBlankLineRead() throws Exception {
        String content = "line\r\n\r\notherthings";
        HttpInputStream s = httpInputStream(12, content);

        assertThat(s.readBody(1), nullValue());
        assertThat(s.readBody(5), nullValue());
        assertThat(s.readBody(1000), nullValue());
    }

    private HttpInputStream httpInputStream(int buffLen, String content) {
        return
            new HttpInputStream(
                buffLen,
                new BufferedInputStream(
                    new ByteArrayInputStream(content.getBytes())));
    }
}
