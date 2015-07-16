package net.zephyrizing.http_server.requests;

import java.nio.file.Files;
import java.nio.file.Path;

import net.zephyrizing.http_server.page.ContentProvider;
import net.zephyrizing.http_server.page.FileContentProvider;
import net.zephyrizing.http_server.page.DirectoryContentProvider;

public class FileSystemHandler {
    private final Path root;

    public FileSystemHandler(Path root) {
        this.root = root;
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