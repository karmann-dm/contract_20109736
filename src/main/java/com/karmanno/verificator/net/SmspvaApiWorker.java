package com.karmanno.verificator.net;

import com.sun.xml.internal.ws.Closeable;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.openqa.selenium.json.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SmspvaApiWorker implements SmsApiWorker {
    private static final String apiKey = "wUkkwsAScZ93OCRyqxlZEYIAYzFEUV";
    private static final String basicUrl = "http://smspva.com/priemnik.php";
    private CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

    private String performRequest(String url, HashMap<String, String> parameters) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url);
        stringBuilder.append("?");
        int index = 0;
        for(Map.Entry<String, String> parameter : parameters.entrySet()) {
            stringBuilder.append(parameter.getKey() + "=" + parameter.getValue());
            if(index != parameters.size() - 1)
                stringBuilder.append("&");
            index++;
        }

        HttpGet httpGet = new HttpGet(stringBuilder.toString());
        CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpGet);
        HttpEntity entity = httpResponse.getEntity();
        return EntityUtils.toString(entity, "UTF-8");
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public void denyNumber(String number) {
        
    }
}
