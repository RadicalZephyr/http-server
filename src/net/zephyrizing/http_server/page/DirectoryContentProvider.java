package net.zephyrizing.http_server.page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DirectoryContentProvider implements ContentProvider {

    private Path content;

    public DirectoryContentProvider(Path content) {
        this.content = content;
    }

    @Override
    public boolean contentExists() {
        return Files.exists(this.content);
    }

    @Override
    public Stream<String> getContent() {
        try {
            return Files.list(this.content)
                .map((Path entry) -> String.format("<a href=\"/%s\">%s</a>",
                                                   entry.toString(),
                                                   entry.getFileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
