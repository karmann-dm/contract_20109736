package com.karmanno.verificator.automation;

public interface NikeAutomation {
    void get();
    void refresh();
    void clickLogin();
    void clickAcceptCookies();
    void dismissChangeCountryAlert();
    void fulfillForm(String username, String password);
}
