package net.zephyrizing.http_server_test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.Socket;

import net.zephyrizing.http_server.HttpConnection;
import net.zephyrizing.http_server.HttpConnectionImpl;

import org.hamcrest.Matcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class HttpConnectionClosingTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    String[] requestStrings = new String[] { "GET / HTTP/1.1",
                                             "" };

    TestSocket socket;
    TestableBufferedReader socketIn;
    TestableByteArrayOutputStream socketOut;

    HttpConnection connection;

    class TestSocket extends Socket {
        public boolean closeCalled = false;

        @Override
        public void close() {
            closeCalled = true;
        }
    }

    class TestableBufferedReader extends BufferedReader {
        public TestableBufferedReader(Reader in) {
            super(in);
        }

        public boolean closeCalled = false;

        @Override
        public void close() throws IOException {
            super.close();
            closeCalled = true;
        }
    }

    class TestableByteArrayOutputStream extends ByteArrayOutputStream {
        public boolean closeCalled = false;

        @Override
        public void close() throws IOException {
            super.close();
            closeCalled = true;
        }
    }

    @Before
    public void initialize() throws IOException {
        socket    = new TestSocket();
        socketIn  = new TestableBufferedReader(new StringReader(""));
        socketOut = new TestableByteArrayOutputStream();

        try (HttpConnection localConn = new HttpConnectionImpl(socket, socketIn, socketOut);) {
            connection = localConn;
        }
    }

    Matcher<Boolean> isTrue = equalTo(true);

    @Test
    public void closesSocket() throws Exception {
        assertThat(socket.closeCalled, isTrue);
    }

    @Test
    public void closesReader() throws Exception {
        assertThat(socketIn.closeCalled, isTrue);
    }

    @Test
    public void closesWriter() throws Exception {
        assertThat(socketOut.closeCalled, isTrue);
    }
}
