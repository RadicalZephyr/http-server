package net.zephyrizing.http_server_test;

import net.zephyrizing.http_server_test.content.FileContentProviderTest;
import net.zephyrizing.http_server_test.content.DirectoryContentProviderTest;

import net.zephyrizing.http_server_test.handlers.CobSpecHandlerTest;
import net.zephyrizing.http_server_test.handlers.FileSystemHandlerTest;
import net.zephyrizing.http_server_test.handlers.RouterTest;
import net.zephyrizing.http_server_test.handlers.SequentialHandlerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HttpConnectionClosingTest.class,
            HttpConnectionTest.class,
            HttpProtocolTest.class,
            HttpRequestTest.class,
            RequestBuilderTest.class,
            HttpResponseTest.class,
            HttpInputStreamTest.class,
            HttpServerSocketImplTest.class,
            HttpServerTest.class,
            FileContentProviderTest.class,
            DirectoryContentProviderTest.class,
            CobSpecHandlerTest.class,
            FileSystemHandlerTest.class,
            RouterTest.class,
            SequentialHandlerTest.class})
public class UnitTests {

}
