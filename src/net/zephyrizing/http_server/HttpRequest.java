package net.zephyrizing.http_server;

public class HttpRequest {

    private String method;
    private String path;
    private String protocolVersion;

    public HttpRequest(String[] request) {
        String firstLine = request[0];
        String[] methodPathProto = firstLine.split(" ");
        this.method          = methodPathProto[0];
        this.path            = methodPathProto[1];
        this.protocolVersion = methodPathProto[2].replace("HTTP/", "");
    }

    public String method() {
        return method;
    }

    public String path() {
        return path;
    }

    public String protocolVersion() {
        return protocolVersion;
    }
}
