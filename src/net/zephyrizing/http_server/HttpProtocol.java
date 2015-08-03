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

        String firstLine = linesList.get(0);
        String[] methodPathProto = firstLine.split(" ");

        if (methodPathProto.length != 3) {
            return null;
        }

        Method method;
        try {
            method = Method.valueOf(methodPathProto[0]);
        } catch (IllegalArgumentException e) {
            return null;
        }

        String path            = methodPathProto[1];
        String protocolVersion = methodPathProto[2].replace("HTTP/", "");

        return new HttpRequest(method, path, protocolVersion);
    }

    public static InputStream responseStream(HttpResponse response) {
        String headStr = Stream.concat(response.getStatusLineStream(),
                                       response.getHeaderStream())
            .collect(Collectors.joining("\r\n", "", "\r\n\r\n"));

        InputStream head = new ByteArrayInputStream(headStr.getBytes());
        InputStream body = response.getData();

        return new SequenceInputStream(head, body);
    }
}
