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

public class SmspvaApiWorker implements SmsApiWorker {
    private static final String apiKey = "wUkkwsAScZ93OCRyqxlZEYIAYzFEUV";
    private static final String basicUrl = "http://smspva.com/priemnik.php";
    private CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

    @Override
    public String getPhoneNumber() {
        StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(basicUrl);
        uriBuilder.append("?");
        uriBuilder.append("method=get_number&");
        uriBuilder.append("country=UK&");
        uriBuilder.append("service=opt86&");
        uriBuilder.append("apikey=" + apiKey);
        HttpGet httpGet = new HttpGet(uriBuilder.toString());
        String out = null;
        try {
            CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            out = EntityUtils.toString(entity, "UTF-8");
            JSONObject jsonObject = new JSONObject(out);
            if(jsonObject.getInt("response") == 1) {
                return jsonObject.getString("number");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public void denyNumber(String number) {
        
    }
}
