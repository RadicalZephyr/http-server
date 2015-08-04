package net.zephyrizing.http_server;

import java.io.BufferedInputStream;
import java.io.IOException;

public class HttpInputStream {

    private BufferedInputStream stream;

    public HttpInputStream(BufferedInputStream stream) {
        this.stream = stream;
    }

    public String readLine() throws IOException {
        int len = 5;
        byte buff[] = new byte[len];

        this.stream.read(buff, 0, len);
        this.stream.skip(2);
        return new String(buff);
    }
}
