package net.zephyrizing.http_server.page;

import java.util.stream.Stream;

public interface ContentProvider {

    public boolean contentExists();

    public Stream<String> getContent();
}
