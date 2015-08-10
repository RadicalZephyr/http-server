package net.zephyrizing.http_server_test;

import net.zephyrizing.http_server.HeadersMap;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HeadersMapTest {

    HeadersMap headers;

    @Before
    public void initialize() {
        headers = new HeadersMap();
    }

    @Test
    public void canAddSimpleValues() {
        headers.addHeader("thing", "stuff");

        assertThat(headers.get("thing"), equalTo("stuff"));
    }

    @Test
    public void cantAddNullValues() {
        headers.addHeader("thing", null);

        assertThat(headers.containsKey("thing"), equalTo(false));
    }

    @Test
    public void concatenatesMultipleValues() {
        headers.addHeader("thing", "one");
        headers.addHeader("thing", "two");

        assertThat(headers.get("thing"), equalTo("one,two"));
    }
}
