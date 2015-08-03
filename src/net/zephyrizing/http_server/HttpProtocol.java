package net.zephyrizing.http_server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

public class HttpProtocol {
    public static HttpRequest requestFromLines(Stream<String> lines) {
        List<String> linesList = lines.filter(Predicate.isEqual("").negate())
            .limit(1).collect(Collectors.toList());

        if (linesList.isEmpty()) {
            return null;
        }

        String requestLine = linesList.get(0);
        RequestBuilder b = new RequestBuilder();

        try {
            parseRequestLine(b, requestLine);
        } catch (IllegalArgumentException e) {
            return null;
        }

        return b.build();
    }

    public static InputStream responseStream(HttpResponse response) {
        String headStr = Stream.concat(response.getStatusLineStream(),
                                       response.getHeaderStream())
            .collect(Collectors.joining("\r\n", "", "\r\n\r\n"));

        InputStream head = new ByteArrayInputStream(headStr.getBytes());
        InputStream body = response.getData();

        return new SequenceInputStream(head, body);
    }

    private static void parseRequestLine(RequestBuilder b, String requestLine) {
        String[] methodPathProto = requestLine.split(" ");

        if (methodPathProto.length != 3) {
            throw new IllegalArgumentException();
        }

        b.method(Method.valueOf(methodPathProto[0]))
            .path(methodPathProto[1])
            .protocolVersion(methodPathProto[2].replace("HTTP/", ""));
    }
}
