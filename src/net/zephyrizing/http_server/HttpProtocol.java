package net.zephyrizing.http_server;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.exceptions.HttpServerException;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

public class HttpProtocol {

    public static final int MAJOR_VERSION = 1;
    public static final int MINOR_VERSION = 1;

    public static final Collector<CharSequence, ?, String> collectAsHttpHeader = Collectors.joining("\r\n", "", "\r\n\r\n");

    public static final Pattern HEADER_RE = Pattern.compile("^([^\\s:]+):\\s*(.*?)\\s*$");

    public static HttpRequest requestFromInputStream(InputStream inStream)
        throws HttpServerException {

        HttpInputStream stream =
            new HttpInputStream(
                new BufferedInputStream(inStream));
        RequestBuilder builder = new RequestBuilder();

        try {
            String tempStr = stream.readLine();
            if (tempStr == null) {
                return null;
            }
            parseRequestLine(builder, tempStr);

            List<String> headerLines = new ArrayList<String>();
            tempStr = stream.readLine();
            while (tempStr != null) {
                headerLines.add(tempStr);
                tempStr = stream.readLine();
            }
            parseRequestHeaders(builder, headerLines);

            if (builder.hasContentHeader()) {
                ByteBuffer body = stream.readBody(builder.contentLength());
                builder.body(body);
            }
        } catch (URISyntaxException e) {
            return null;
        } catch (IOException e) {
            return null;
        } catch (IllegalArgumentException e) {
            return null;
        }

        return builder.build();
    }

    public static InputStream responseStream(HttpResponse response) {
        String headStr = Stream.concat(response.getStatusLineStream(),
                                       response.getHeaderStream())
            .collect(collectAsHttpHeader);

        InputStream head = new ByteArrayInputStream(headStr.getBytes());
        InputStream body = response.getData();

        return new SequenceInputStream(head, body);
    }

    private static void parseRequestLine(RequestBuilder builder, String requestLine)
        throws URISyntaxException {

        String[] methodPathProto = requestLine.split(" ");

        if (methodPathProto.length != 3) {
            throw new IllegalArgumentException();
        }

        builder.method(Method.valueOf(methodPathProto[0]));
        URI uri = new URI(methodPathProto[1]);
        builder.path(uri.getPath())
            .query(uri.getRawQuery());
    }

    private static void parseRequestHeaders(RequestBuilder builder, List<String> headerLines) {
        headerLines.forEach(s -> parseRequestHeader(builder, s));
    }

    private static void parseRequestHeader(RequestBuilder builder,  String headerLine) {
        Matcher m = HEADER_RE.matcher(headerLine);
        if (m.matches()) {
            String headerKey = m.group(1);
            String headerValueStr = m.group(2);

            // This shouldn't really be done here. Header splitting isn't quite this generic
            String headerValues[] = headerValueStr.split("\\s*,\\s*");
            builder.header(headerKey, Arrays.asList(headerValues));
        }
    }
}
