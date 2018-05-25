package com.karmanno.verificator.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import javax.swing.*;

public class ChromeNikeAutomation implements NikeAutomation {
    private static final String address = "www.nike.com/gb/en_gb/";
    private WebDriver webDriver;
    private Actions builder;

    public ChromeNikeAutomation(ChromeDriver chromeDriver) {
        setWebDriver(chromeDriver);
    }

    public void setWebDriver(ChromeDriver webDriver) {
        this.webDriver = webDriver;
        builder = new Actions(webDriver);
    }

    public ChromeDriver getWebDriver() {
        return (ChromeDriver)webDriver;
    }

    @Override
    public void get() {
        webDriver.get(address);
    }

    @Override
    public void refresh() {
        webDriver.navigate().refresh();
    }

    @Override
    public void clickLogin() {
        WebElement loginButton = webDriver.findElement(By.cssSelector("span[class='login-text']"));
        builder.moveToElement(loginButton, 0, 0).click().build().perform();
    }

    @Override
    public void clickAcceptCookies() {

    }

    @Override
    public void dismissChangeCountryAlert() {

    }

    @Override
    public void fulfillForm(String username, String password) {

    }
}
