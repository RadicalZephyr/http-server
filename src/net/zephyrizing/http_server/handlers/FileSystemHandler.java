package net.zephyrizing.http_server.handlers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import net.zephyrizing.http_server.HttpRequest;
import net.zephyrizing.http_server.HttpResponse;
import net.zephyrizing.http_server.content.ContentProvider;
import net.zephyrizing.http_server.content.DirectoryContentProvider;
import net.zephyrizing.http_server.content.FileContentProvider;

public class FileSystemHandler implements Handler {
    private final Path root;

    public FileSystemHandler(Path root) {
        this.root = root;
    }

    public HttpResponse handle(HttpRequest request) {
        HttpResponse response = HttpResponse.responseFor(request);
        response.setContent(getContentFor(Paths.get("/").relativize(request.path())));
        return response;
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
