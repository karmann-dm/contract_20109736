package com.karmanno.verificator.net;

public interface SmsApiWorker {
    String getPhoneNumber();
    String getCode();
    void denyNumber(String number);
}
