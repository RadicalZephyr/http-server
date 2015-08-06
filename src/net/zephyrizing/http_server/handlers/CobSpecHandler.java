package net.zephyrizing.http_server.handlers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Collectors;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.content.ContentProvider;
import net.zephyrizing.http_server.content.StringContentProvider;
import net.zephyrizing.http_server.middleware.UrlParams;

import static net.zephyrizing.http_server.HttpRequest.Method.*;

public class CobSpecHandler implements Handler {
    Handler handler;

    public CobSpecHandler(Path public_root) {
        Handler fileHandler = new FileSystemHandler(public_root);
        Router testHandler = new Router();
        Handler nullHandler = new NullHandler();

        testHandler.addHandler(GET,  "/redirect",
                               (HttpRequest r) -> {
                                   HttpResponse response = new HttpResponse();
                                   response.setStatus(302);
                                   response.addHeader("Location", "http://localhost:5000/");
                                   return response;
                               });

        testHandler.addHandler(GET,  "/parameters", UrlParams.wrap(this::returnDecodedParams));

        testHandler.addHandler(GET,  "/method_options", nullHandler);
        testHandler.addHandler(HEAD, "/method_options", nullHandler);
        testHandler.addHandler(POST, "/method_options", nullHandler);
        testHandler.addHandler(PUT,  "/method_options", nullHandler);

        testHandler.addHandler(GET,  "/form", this::getData);
        testHandler.addHandler(POST, "/form", this::storeData);
        testHandler.addHandler(PUT,  "/form", this::storeData);
        testHandler.addHandler(DELETE,  "/form", this::deleteData);

        this.handler = new SequentialHandler(testHandler, fileHandler);
    }

    @Override
    public HttpResponse handle(HttpRequest r) {
        return handler.handle(r);
    }

    private String data = "";

    public HttpResponse returnDecodedParams(HttpRequest request) {
        HttpResponse response = new HttpResponse();
        String body = request.urlParams().entrySet().stream()
            .map(entry ->
                 String.format("%s = %s", entry.getKey(), entry.getValue()))
            .collect(Collectors.joining("\n"));
        response.setContent(new StringContentProvider(body));
        return response;
    }

    public HttpResponse getData(HttpRequest r) {
        HttpResponse response = new HttpResponse();
        response.setContent(new StringContentProvider(this.data));

        return response;
    }


    public HttpResponse storeData(HttpRequest request) {
        this.data = request.bodyAsText();
        return new HttpResponse();
    }

    public HttpResponse deleteData(HttpRequest request) {
        this.data = "";
        return new HttpResponse();
    }
}
