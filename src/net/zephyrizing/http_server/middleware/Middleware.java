package net.zephyrizing.http_server.middleware;

import net.zephyrizing.http_server.handlers.Handler;

public interface Middleware {

    public Handler wrap(Handler h);
}
