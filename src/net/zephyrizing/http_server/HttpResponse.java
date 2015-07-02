package net.zephyrizing.http_server;

public class HttpResponse {

    public static HttpResponse responseFor(HttpRequest request) {
        return new HttpResponse();
    }

    public String protocolVersion() {
        return "1.1";
    }
}
