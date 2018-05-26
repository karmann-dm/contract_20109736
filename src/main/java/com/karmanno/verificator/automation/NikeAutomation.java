package com.karmanno.verificator.automation;

public interface NikeAutomation {
    void get() throws InterruptedException;
    void refresh() throws InterruptedException;
    void clickLogin() throws InterruptedException;
    void clickAcceptCookies() throws InterruptedException;
    void dismissChangeCountryAlert() throws InterruptedException;
    void fulfillForm(String username, String password) throws InterruptedException;
    void getSettings() throws InterruptedException;
    void clickAddPhone() throws InterruptedException;
    void checkLegalTerms() throws InterruptedException;
    void enterPhone(String phone) throws InterruptedException;
    void pressVerify() throws InterruptedException;
    void pressGetCode() throws InterruptedException;
    void enterCode(String code) throws InterruptedException;
}
