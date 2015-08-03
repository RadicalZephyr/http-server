package net.zephyrizing.http_server.handlers;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.stream.Collectors;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import static net.zephyrizing.http_server.HttpRequest.Method.*;
import net.zephyrizing.http_server.content.ContentProvider;

public class CobSpecHandler implements Handler {
    Handler handler;

    public CobSpecHandler(Path public_root) {
        Handler fileHandler = new FileSystemHandler(public_root);
        Router testHandler = new Router();
        Handler nullHandler = new NullHandler();

        testHandler.addHandler(GET,  "/", (HttpRequest r) -> HttpResponse.responseFor(r));

        testHandler.addHandler(GET,  "/redirect",
                               (HttpRequest r) -> {
                                   HttpResponse response = HttpResponse.responseFor(r);
                                   response.setStatus(302);
                                   response.addHeader("Location", "http://localhost:5000/");
                                   return response;
                               });

        testHandler.addHandler(GET,  "/method_options", nullHandler);
        testHandler.addHandler(HEAD, "/method_options", nullHandler);
        testHandler.addHandler(POST, "/method_options", nullHandler);
        testHandler.addHandler(PUT,  "/method_options", nullHandler);

        testHandler.addHandler(GET,  "/form", this::getData);
        testHandler.addHandler(POST, "/form", this::storeData);
        testHandler.addHandler(PUT,  "/form", this::storeData);

        this.handler = new SequentialHandler(testHandler, fileHandler);
    }

    @Override
    public HttpResponse handle(HttpRequest r) {
        return handler.handle(r);
    }

    private String data = "";

    public HttpResponse getData(HttpRequest r) {
        HttpResponse response = HttpResponse.responseFor(r);
        response.setContent(new ContentProvider() {
                @Override
                public boolean contentExists() { return true;}

                @Override
                public InputStream getContent() { return new ByteArrayInputStream(data.getBytes());}
            });

        return response;
    }

    public HttpResponse storeData(HttpRequest request) {
        BufferedReader r = new BufferedReader(new InputStreamReader(request.body()));
        this.data = r.lines().collect(Collectors.joining());
        return HttpResponse.responseFor(request);
    }
}
