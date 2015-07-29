package net.zephyrizing.http_server.content;

import java.io.InputStream;

public interface ContentProvider {

    public boolean contentExists();

    public InputStream getContent();
}
