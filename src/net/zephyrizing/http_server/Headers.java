package net.zephyrizing.http_server;

import java.util.Map;

public interface Headers extends Map<String, String> {

    public void addHeader(String name, String value);
}
