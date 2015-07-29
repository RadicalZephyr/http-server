package net.zephyrizing.http_server_test;

import net.zephyrizing.http_server_test.content.FileContentProviderTest;
import net.zephyrizing.http_server_test.content.DirectoryContentProviderTest;

import net.zephyrizing.http_server_test.routing.FileSystemHandlerTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HttpConnectionClosingTest.class,
            HttpConnectionTest.class,
            HttpProtocolTest.class,
            HttpRequestTest.class,
            HttpResponseTest.class,
            HttpServerSocketImplTest.class,
            HttpServerTest.class,
            FileContentProviderTest.class,
            DirectoryContentProviderTest.class,
            FileSystemHandlerTest.class})
public class UnitTests {

}
