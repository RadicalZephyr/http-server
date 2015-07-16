package net.zephyrizing.http_server.page;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class DirectoryContentProvider implements ContentProvider {

    private final Path root;
    private final Path content;

    public DirectoryContentProvider(Path root, Path content) {
        this.root = root;
        this.content = root.resolve(content);
    }

    @Override
    public boolean contentExists() {
        return Files.exists(this.content);
    }

    @Override
    public Stream<String> getContent() {
        try {
            return Files.list(this.content)
                .map((Path entry) -> String.format("<a href=\"/%s\">%s</a><br>",
                                                   this.root.relativize(entry).toString(),
                                                   entry.getFileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
