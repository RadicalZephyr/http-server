package net.zephyrizing.http_server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zephyrizing.http_server.HttpRequest.Method;

public class RequestBuilder {

    private static final String CONTENT_LENGTH_HEADER = "Content-Length";
    private Method m;
    private String p;
    private String v;

    private Map<String, List<String>> headers = new HashMap<String, List<String>>();

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

    public RequestBuilder header(String key, List<String> values) {
        this.headers.put(key, values);
        return this;
    }

    public boolean hasContentHeader() {
        return this.headers.containsKey(CONTENT_LENGTH_HEADER);
    }

    public long contentLength() {
        return Integer.parseInt(this.headers.get(CONTENT_LENGTH_HEADER).get(0));
    }

    public HttpRequest build() {
        assertNotNull(this.m, "Request method must be set");
        assertNotNull(this.p, "Request path must be set");
        assertNotNull(this.v, "Protocol version must be set");
        return new HttpRequest(this.m, this.p, this.v, this.headers, null);
    }

    private void assertNotNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(String.format("%s before calling build.", message));
        }
    }
}
