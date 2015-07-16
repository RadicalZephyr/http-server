package net.zephyrizing.http_server.page;

import java.util.stream.Stream;

public interface ContentProvider {
    Stream<String> getContent();
}
