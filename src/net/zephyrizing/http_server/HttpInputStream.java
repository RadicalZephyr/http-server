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
        this.stream.mark(buffLen);

        StringBuilder line = new StringBuilder();
        byte buff[] = new byte[buffLen];

        int bytesRead = this.stream.read(buff, 0, buffLen);
        line.append(decode(buff, bytesRead)).toString();

        int crlfIndex = line.indexOf("\r\n");
        if (crlfIndex == -1) {
            crlfIndex = Integer.MAX_VALUE;
        }

        int lineLength = Math.min(crlfIndex, bytesRead);

        this.stream.reset();
        this.stream.skip(lineLength+2);

        return line.substring(0, lineLength);
    }

    private CharSequence decode(byte buff[], int len) {
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(buff, 0, len));
    }
}
