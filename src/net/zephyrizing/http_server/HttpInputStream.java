package net.zephyrizing.http_server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HttpInputStream {

    private int buffLen;
    private BufferedInputStream stream;

    public HttpInputStream(BufferedInputStream stream) {
        this(1024*4, stream);
    }

    public HttpInputStream(int buffLen, BufferedInputStream stream) {
        this.buffLen = buffLen;
        this.stream = stream;
    }

    public String readLine() throws IOException {
        byte buff[] = new byte[buffLen];

        int bytesRead = this.stream.read(buff, 0, buffLen);

        this.stream.skip(2);
        return new StringBuilder().append(decode(buff, bytesRead)).toString();
    }

    private CharSequence decode(byte buff[], int len) {
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(buff, 0, len));
    }
}
