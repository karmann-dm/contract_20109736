package com.karmanno.verificator.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class ChromeNikeAutomation implements NikeAutomation {
    private static final String address = "https://www.nike.com/gb/en_gb/";
    private WebDriver webDriver;
    private Actions builder;

    public ChromeNikeAutomation(WebDriver chromeDriver) {
        setWebDriver(chromeDriver);
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
        builder = new Actions(webDriver);
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    @Override
    public void get() throws InterruptedException {
        webDriver.get(address);
        Thread.sleep(4000);
    }

    @Override
    public void refresh() throws InterruptedException {
        webDriver.navigate().refresh();
        Thread.sleep(5000);
    }

    @Override
    public void clickLogin() throws InterruptedException {
        WebElement loginButton = webDriver.findElement(By.cssSelector("span[class='login-text']"));
        builder.moveToElement(loginButton).click().build().perform();
        Thread.sleep(5000);
    }

    @Override
    public void clickAcceptCookies() throws InterruptedException {
        WebElement acceptButton = webDriver.findElement(By.cssSelector("button[class='nsg-button nsg-grad--nike-orange yes-button cookie-settings-button js-yes-button']"));
        builder.moveToElement(acceptButton, 0, 0).click().build().perform();
        Thread.sleep(5000);
    }

    @Override
    public void dismissChangeCountryAlert() throws InterruptedException {
        clickLogin();
    }

    @Override
    public void fulfillForm(String username, String password) throws InterruptedException {
        WebElement usernameInput = webDriver.findElement(By.cssSelector("input[type='email']"));
        WebElement passwordInput = webDriver.findElement(By.cssSelector("input[type='password']"));

        usernameInput.click();
        usernameInput.clear();
        usernameInput.sendKeys(username);

        passwordInput.click();
        passwordInput.clear();
        passwordInput.sendKeys(password);

        passwordInput.sendKeys(Keys.ENTER);

        Thread.sleep(1000);
    }
}
