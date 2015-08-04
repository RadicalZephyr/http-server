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

    @Test
    public void canReadVaryingLengthLines() throws Exception {
        String content = "short\r\ntoolong\r\n";
        HttpInputStream s =
            new HttpInputStream(
                7,
                new BufferedInputStream(
                    new ByteArrayInputStream(content.getBytes())));

        assertThat(s.readLine(), equalTo("short"));
        assertThat(s.readLine(), equalTo("toolong"));
    }

    @Test
    public void canReadLinesTwiceAsBigAsBuffer() throws Exception {
        String content = "muchtoolong!\r\n";
        HttpInputStream s =
            new HttpInputStream(
                7,
                new BufferedInputStream(
                    new ByteArrayInputStream(content.getBytes())));

        System.out.println("twiceAsLongTest");
        assertThat(s.readLine(), equalTo("muchtoolong!"));
    }

    @Test
    public void returnsNullOnEmptyLineRead() throws Exception {
        String content = "line1\r\nline2\r\nline3\r\n\r\n";
        HttpInputStream s =
            new HttpInputStream(
                12,
                new BufferedInputStream(
                    new ByteArrayInputStream(content.getBytes())));

        assertThat(s.readLine(), equalTo("line1"));
        assertThat(s.readLine(), equalTo("line2"));
        assertThat(s.readLine(), equalTo("line3"));
        assertThat(s.readLine(), nullValue());
    }

    @Test
    public void canReadCRAndLFInSeparateBuffers() throws Exception {
        String content = "line\r\notherstuff\r\n";
        HttpInputStream s =
            new HttpInputStream(
                5,
                new BufferedInputStream(
                    new ByteArrayInputStream(content.getBytes())));

        assertThat(s.readLine(), equalTo("line"));
        assertThat(s.readLine(), equalTo("otherstuff"));
    }

    @Test
    public void stopsReadingByLinesAfterBlankLine() throws Exception {
        String content = "line\r\n\r\notherthings\r\nthatareneverread\r\n";
        HttpInputStream s =
            new HttpInputStream(
                12,
                new BufferedInputStream(
                    new ByteArrayInputStream(content.getBytes())));

        assertThat(s.readLine(), equalTo("line"));
        assertThat(s.readLine(), nullValue());
        assertThat(s.readLine(), nullValue());
        assertThat(s.readLine(), nullValue());
    }
}
