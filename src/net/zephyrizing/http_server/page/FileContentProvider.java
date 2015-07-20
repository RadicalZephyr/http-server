package net.zephyrizing.http_server.page;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileContentProvider implements ContentProvider {
    private final Path content;

    public FileContentProvider(Path content) {
        this.content = content;
    }

    @Override
    public boolean contentExists() {
        return Files.exists(this.content);
    }

    @Override
    public InputStream getContent() {
        try {
            return Files.newInputStream(this.content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
