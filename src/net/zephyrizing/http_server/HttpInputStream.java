package net.zephyrizing.http_server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class HttpInputStream {

    private int buffLen;
    private BufferedInputStream stream;

    private boolean textSectionEnded = false;

    public HttpInputStream(BufferedInputStream stream) {
        this(1024*4, stream);
    }

    public HttpInputStream(int buffLen, BufferedInputStream stream) {
        this.buffLen = buffLen;
        this.stream = stream;
    }

    public ByteBuffer readBody(int length) throws IOException {
        if (!textSectionEnded) return null;

        byte buff[] = new byte[length];
        int bytesRead = this.stream.read(buff, 0, length);

        return ByteBuffer.wrap(buff, 0, bytesRead);
    }

    public String readLine() throws IOException {
        if (textSectionEnded) return null;

        StringBuilder line = new StringBuilder();
        byte buff[] = new byte[buffLen];
        int crlfIndex = Integer.MAX_VALUE;
        int lastMark = -1;

        do {
            this.stream.mark(buffLen);
            lastMark = line.length();

            int bytesRead = this.stream.read(buff, 0, buffLen);
            if (bytesRead == -1) {
                throw new IOException("Unexpected end of input.");
            }
            line.append(decode(buff, bytesRead));

            crlfIndex = line.indexOf("\r\n");
            if (crlfIndex == -1) {
                crlfIndex = Integer.MAX_VALUE;
            }

        } while (crlfIndex == Integer.MAX_VALUE);

        int lineLength = crlfIndex;
        int skipLength = lineLength - lastMark;

        this.stream.reset();
        this.stream.skip(skipLength+2);

        if (lineLength != 0) {
            return line.substring(0, lineLength);
        } else {
            textSectionEnded = true;
            return null;
        }
    }

    private CharSequence decode(byte buff[], int len) {
        return StandardCharsets.UTF_8.decode(ByteBuffer.wrap(buff, 0, len));
    }
}
