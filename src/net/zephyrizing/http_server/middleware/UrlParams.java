package net.zephyrizing.http_server.middleware;

import net.zephyrizing.http_server.handlers.Handler;

public class UrlParams {

    public static Handler wrap(Handler h) {
        return h;
    }
}
