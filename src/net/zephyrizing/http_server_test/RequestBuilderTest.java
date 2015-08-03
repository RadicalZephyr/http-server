package net.zephyrizing.http_server_test;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.RequestBuilder;
import static  net.zephyrizing.http_server.HttpRequest.Method.*;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class RequestBuilderTest {

    @Test
    public void canAddAMethod() {
        Method m = GET;
        String p = "/";
        String v = "1.1";

        HttpRequest request = new RequestBuilder()
            .method(m)
            .path(p)
            .protocolVersion(v)
            .build();

        assertThat(request.method(),          equalTo(m));
        assertThat(request.path().toString(), equalTo(p));
        assertThat(request.protocolVersion(), equalTo(v));
    }

}
