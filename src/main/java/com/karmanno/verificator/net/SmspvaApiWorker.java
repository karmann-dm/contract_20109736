package com.karmanno.verificator.net;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
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
    public JSONObject getPhoneNumber() {
        HashMap<String, String> parametersMap = new HashMap<String, String>();
        parametersMap.put("method", "get_number");
        parametersMap.put("country", "UK");
        parametersMap.put("service", "opt86");
        parametersMap.put("apikey", apiKey);
        try {
            JSONObject jsonObject = new JSONObject(performRequest(basicUrl, parametersMap));
            if(jsonObject.getInt("response") == 1)
                return jsonObject;
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getCode(String number) {
        HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("method", "get_sms");
        parametersMap.put("country", "UK");
        parametersMap.put("service", "opt86");
        parametersMap.put("id", number);
        parametersMap.put("apikey", apiKey);
        try {
            JSONObject jsonObject = new JSONObject(performRequest(basicUrl, parametersMap));
            int response = jsonObject.getInt("response");
            System.out.println("Response: " + response);
            int countdown = 20;
            while (response != 1) {
                jsonObject = new JSONObject(performRequest(basicUrl, parametersMap));
                response = jsonObject.getInt("response");
                System.out.println("Response: " + response);
                Thread.sleep(1000);
                countdown--;

                if(countdown < 0)
                    return null;
            }
            System.out.println(jsonObject.getString("sms"));
            return jsonObject.getString("sms");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void denyNumber(String number) {
        
    }
}
