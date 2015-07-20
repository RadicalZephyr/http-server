package net.zephyrizing.http_server.page;

import java.io.InputStream;

public interface ContentProvider {

    public boolean contentExists();

    public InputStream getContent();
}
