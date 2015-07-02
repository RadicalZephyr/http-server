package net.zephyrizing.http_server;

import net.zephyrizing.http_server.requests.RequestBuilder;

import java.util.List;

public class HttpRequest {
    public static enum Method {GET, POST};

    private Method method;
    private String path;
    private String protocolVersion;

    public HttpRequest(Method method, String path, String protocolVersion) {
        this.method          = method;
        this.path            = path;
        this.protocolVersion = protocolVersion;
    }

    public Method method() {
        return method;
    }

    public String path() {
        return path;
    }

    public String protocolVersion() {
        return protocolVersion;
    }
}
