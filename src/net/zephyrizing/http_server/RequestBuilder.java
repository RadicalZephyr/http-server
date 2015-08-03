package net.zephyrizing.http_server;

import net.zephyrizing.http_server.HttpRequest.Method;

public class RequestBuilder {

    private Method m;
    private String p;
    private String v;

    public RequestBuilder method(Method m) {
        this.m = m;
        return this;
    }

    public RequestBuilder path(String p) {
        this.p = p;
        return this;
    }

    public RequestBuilder protocolVersion(String v) {
        this.v = v;
        return this;
    }

    public HttpRequest build() {
        assertNotNull(this.m, "Request method must be set");
        assertNotNull(this.p, "Request path must be set");
        assertNotNull(this.v, "Protocol version must be set");
        return new HttpRequest(this.m, this.p, this.v);
    }

    private void assertNotNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(String.format("%s before calling build.", message));
        }
    }
}
