package net.zephyrizing.http_server.exceptions;

public class HttpServerException extends Exception {

    public HttpServerException() {
        super();
    }

    public HttpServerException(String message) {
        super(message);
    }

    public HttpServerException(Throwable cause) {
        super(cause);
    }

    public HttpServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
