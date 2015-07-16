package net.zephyrizing.http_server.page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

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
    public Stream<String> getContent() {
        try {
            return Files.lines(this.content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
