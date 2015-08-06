package net.zephyrizing.http_server.middleware;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.zephyrizing.http_server.handlers.Handler;

public class Middlewares {
    public static Handler wrapWithAll(Handler handler, Middleware... wares) {
        Handler temp = handler;

        List<Middleware> middlewares = Arrays.asList(wares);
        Collections.reverse(middlewares);

        for(Middleware m : middlewares) {
            temp = m.wrap(temp);
        }

        return temp;
    }
}
