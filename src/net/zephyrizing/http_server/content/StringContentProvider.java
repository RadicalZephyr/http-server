package net.zephyrizing.http_server.content;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringContentProvider implements ContentProvider {

    private String content;

    public StringContentProvider(String content) {
        this.content = content;
    }

    @Override
    public boolean contentExists() {
        return this.content != null;
    }

    @Override
    public InputStream getContent() {
        if (contentExists()) {
            return new ByteArrayInputStream(content.getBytes());
        } else {
            return new ByteArrayInputStream(new byte[0]);
        }
    }
}
