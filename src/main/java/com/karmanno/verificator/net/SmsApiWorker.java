package com.karmanno.verificator.net;

import org.json.JSONObject;

public interface SmsApiWorker {
    JSONObject getPhoneNumber();
    String getCode(String number);
    void denyNumber(String number);
}
