package net.zephyrizing.http_server.content;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class EmptyContentProvider implements ContentProvider {

    @Override
    public boolean contentExists() {
        return true;
    }

    @Override
    public InputStream getContent() {
        return new ByteArrayInputStream(new byte[0]);
    }
}
