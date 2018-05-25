package com.karmanno.verificator.automation;

public interface NikeAutomation {
    void get() throws InterruptedException;
    void refresh() throws InterruptedException;
    void clickLogin() throws InterruptedException;
    void clickAcceptCookies() throws InterruptedException;
    void dismissChangeCountryAlert() throws InterruptedException;
    void fulfillForm(String username, String password) throws InterruptedException;
}
