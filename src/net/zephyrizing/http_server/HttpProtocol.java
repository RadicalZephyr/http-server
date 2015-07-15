package net.zephyrizing.http_server;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

public class HttpProtocol {
    public static HttpRequest requestFromLines(List<String> lines) {
        String firstLine = lines.get(0);
        String[] methodPathProto = firstLine.split(" ");
        Method method          = Method.valueOf(methodPathProto[0]);
        String path            = methodPathProto[1];
        String protocolVersion = methodPathProto[2].replace("HTTP/", "");

        return new HttpRequest(method, path, protocolVersion);
    }

    public static Stream<String> responseStream(HttpResponse response) {
        Stream<String> statusLine = emptyStringStream()
            .add(String.format("HTTP/%s 200 OK", response.protocolVersion()))
            .build();
        Stream<String> heading = Stream.concat(statusLine, response.getHeaderStream());

        Stream<String> body = Stream.concat(emptyLine(), response.getDataStream());
        Stream<String> responseStream = Stream.concat(heading, body);

        return responseStream;
    }

    private static Stream.Builder<String> emptyStringStream() {
        return Stream.builder();
    }

    private static Stream<String> emptyLine() {
        return emptyStringStream().add("").build();
    }
}
