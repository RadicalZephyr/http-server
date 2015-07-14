package net.zephyrizing.http_server_test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({HttpConnectionClosingTest.class,
            HttpConnectionTest.class,
            HttpProtocolTest.class,
            HttpRequestTest.class,
            HttpResponseTest.class,
            HttpServerSocketImplTest.class,
            HttpServerTest.class})
public class UnitTests {

}
