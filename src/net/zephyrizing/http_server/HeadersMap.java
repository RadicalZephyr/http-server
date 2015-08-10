package net.zephyrizing.http_server;

import java.util.HashMap;

public class HeadersMap extends HashMap<String, String> implements Headers {

    @Override
    public void addHeader(String name, String value) {
        if (value != null) {
            String actualVal = this.getOrDefault(name, "");
            if (actualVal.equals("")) {
                actualVal = value;
            } else {
                actualVal += "," + value;
            }
            this.put(name, actualVal);
        }
    }
}
