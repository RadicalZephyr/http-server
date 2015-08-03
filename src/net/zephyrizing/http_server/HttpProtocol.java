package net.zephyrizing.http_server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

public class HttpProtocol {

    public static final Collector<CharSequence, ?, String> collectAsHttpHeader = Collectors.joining("\r\n", "", "\r\n\r\n");

    public static final Pattern HEADER_RE = Pattern.compile("^([^:]+):\\s*(.*)$");

    public static HttpRequest requestFromInputStream(InputStream stream) {

        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        RequestBuilder b = new RequestBuilder();

        try {
            String s = r.readLine();
            while ("".equals(s)) {
                s = r.readLine();
            }
            if (s == null) {
                return null;
            }
            parseRequestLine(b, s);

            List<String> headerLines = new ArrayList<String>();
            s = r.readLine();
            while (!"".equals(s) && s != null) {
                headerLines.add(s);
                s = r.readLine();
            }
            parseRequestHeaders(b, headerLines);

        } catch (IOException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }

        return b.build();
    }

    public static InputStream responseStream(HttpResponse response) {
        String headStr = Stream.concat(response.getStatusLineStream(),
                                       response.getHeaderStream())
            .collect(collectAsHttpHeader);

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

    private static void parseRequestHeaders(RequestBuilder b, List<String> headerLines) {
        b.header("Content-Length", Collections.singletonList("0"));
    }
}
