package com.karmanno.verificator.io;

import com.karmanno.verificator.net.ProxyFactory;
import org.openqa.selenium.Proxy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ProxyFileLoader implements FileLoader{
    private ProxyFactory proxyFactory = new ProxyFactory();

    @Override
    public ArrayList<Proxy> load(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        ArrayList<Proxy> result = new ArrayList<>();

        while (line != null) {
            String[] credentials = line.split(":");
            if(credentials.length < 4)
                throw new RuntimeException("File has wrong format. Need IP:PORT:USER:PASS");
            result.add(proxyFactory.createProxy(
                    credentials[0],
                    credentials[1],
                    credentials[2],
                    credentials[3]
            ));
            line = reader.readLine();
        }

        return result;
    }

    @Override
    public void save(File file, Object object) throws Exception {
    }
}
