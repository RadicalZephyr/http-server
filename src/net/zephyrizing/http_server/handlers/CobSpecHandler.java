package net.zephyrizing.http_server.handlers;

import java.nio.file.Path;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import static net.zephyrizing.http_server.HttpRequest.Method.*;

public class CobSpecHandler implements Handler {
    Handler handler;

    public CobSpecHandler(Path public_root) {
        Handler fileHandler = new FileSystemHandler(public_root);
        Router testHandler = new Router();
        Handler nullHandler = new NullHandler();

        testHandler.addHandler(GET,  "/method_options", nullHandler);
        testHandler.addHandler(HEAD, "/method_options", nullHandler);
        testHandler.addHandler(POST, "/method_options", nullHandler);
        testHandler.addHandler(PUT,  "/method_options", nullHandler);

        this.handler = new SequentialHandler(testHandler, fileHandler);
    }

    @Override
    public HttpResponse handle(HttpRequest r) {
        return handler.handle(r);
    }
}
