package net.zephyrizing.http_server.handlers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpRequest.Method;
import net.zephyrizing.http_server.HttpResponse;

import net.zephyrizing.http_server.content.ContentProvider;
import net.zephyrizing.http_server.content.DirectoryContentProvider;
import net.zephyrizing.http_server.content.FileContentProvider;

public class FileSystemHandler implements Handler {
    private final Path root;

    public FileSystemHandler(Path root) {
        this.root = root;
    }

    @Override
    public HttpResponse handle(HttpRequest request) {
        if (request.method() == Method.GET) {
            Path relativeRequestPath = Paths.get("/").relativize(request.path());
            HttpResponse response = HttpResponse.responseFor(request);
            ContentProvider cp = getContentFor(relativeRequestPath);
            if (cp == null) {
                response.setStatus(404);
            } else {
                response.setContent(cp);
            }
            return response;
        } else {
            return null;
        }
    }

    public ContentProvider getContentFor(Path path) {
        Path absolutePath = this.root.resolve(path);
        if (Files.isRegularFile(absolutePath)) {
            return new FileContentProvider(absolutePath);
        } else if (Files.isDirectory(absolutePath)) {
            return new DirectoryContentProvider(this.root, path);
        } else {
            return null;
        }
    }
}
