package net.zephyrizing.http_server.content;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryContentProvider implements ContentProvider {

    private static final String HEADER = "<!DOCTYPE html>\n<html><head></head><body>";
    private static final String FOOTER = "</body></html>";

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
    public InputStream getContent() {
        try {
            Stream<String> headings = Stream.of(String.format("<h1>Index of /%s</h1>",
                                                              relativeToRoot(this.content).getFileName()));
            Path parentPath = this.content.getParent();
            if (parentPath.startsWith(this.root)) {
                headings = Stream.concat(headings,
                                         Stream.of(String.format("<a href=\"/%s\">..</a><br>",
                                                                 relativeToRoot(parentPath).toString())));
            }
            byte[] bytes =
                Stream.concat(headings,
                              Files.list(this.content)
                              .map((Path entry) -> String.format("<a href=\"/%s\">%s</a><br>",
                                                                 relativeToRoot(entry).toString(),
                                                                    entry.getFileName())))
                .collect(Collectors.joining("", HEADER, FOOTER)).getBytes();
            return new ByteArrayInputStream(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Path relativeToRoot(Path branch) {
        return this.root.relativize(branch);
    }
}
