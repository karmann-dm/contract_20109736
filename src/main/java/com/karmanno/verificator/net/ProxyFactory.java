package com.karmanno.verificator.net;

import org.openqa.selenium.Proxy;

public class ProxyFactory {
    public Proxy createProxy(String inetAddress, String port, String username, String password) {
        Proxy proxy = new Proxy();

        proxy.setAutodetect(false);
        proxy.setProxyType(Proxy.ProxyType.MANUAL);
        proxy.setHttpProxy(inetAddress + ":" + port);
        proxy.setSslProxy(inetAddress + ":" + port);
        proxy.setSocksUsername(username);
        proxy.setSocksPassword(password);

        return proxy;
    }
}
