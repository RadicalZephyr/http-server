package net.zephyrizing.http_server;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.zephyrizing.http_server.HttpRequest.Method;

public class RequestBuilder {

    private static final String CONTENT_LENGTH_HEADER = "Content-Length";

    private Method m;
    private String p;
    private String q;
    private ByteBuffer buff;

    private Headers headers = new HeadersMap();

    public RequestBuilder method(Method m) {
        this.m = m;
        return this;
    }

    public RequestBuilder path(String p) {
        this.p = p;
        return this;
    }

    public RequestBuilder query(String q) {
        this.q = q;
        return this;
    }

    public RequestBuilder header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public boolean hasContentHeader() {
        return this.headers.containsKey(CONTENT_LENGTH_HEADER);
    }

    public long contentLength() {
        return Integer.parseInt(this.headers.get(CONTENT_LENGTH_HEADER));
    }

    public RequestBuilder body(ByteBuffer buff) {
        this.buff = buff;
        return this;
    }

    public HttpRequest build() {
        assertNotNull(this.m, "Request method must be set");
        assertNotNull(this.p, "Request path must be set");
        return new HttpRequest(this.m, this.p, this.q,
                               this.headers, this.buff);
    }

    private void assertNotNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(String.format("%s before calling build.", message));
        }
    }
}
