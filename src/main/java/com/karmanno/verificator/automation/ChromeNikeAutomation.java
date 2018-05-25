package com.karmanno.verificator.automation;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
        new WebDriverWait(webDriver, 10).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        Thread.sleep(5000);
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
        JavascriptExecutor executor = (JavascriptExecutor)webDriver;
        executor.executeScript("arguments[0].click();", acceptButton);
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

        Thread.sleep(20000);
    }

    @Override
    public void getSettings() throws InterruptedException {
        webDriver.get("https://www.nike.com/gb/en_gb/p/settings");
        new WebDriverWait(webDriver, 20).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
        Thread.sleep(3000);
    }

    @Override
    public void clickAddPhone() throws InterruptedException {
        // mobile-number--add-button nsg-button nsg-grad--light-grey
        WebElement loginButton = webDriver.findElement(By.cssSelector("button[class='mobile-number--add-button nsg-button nsg-grad--light-grey']"));
        builder.moveToElement(loginButton).click().build().perform();
        Thread.sleep(5000);
    }

    @Override
    public void checkLegalTerms() throws InterruptedException {
        WebElement legalCheckBox = webDriver.findElement(By.cssSelector("input[name='legalterms']"));
        builder.moveToElement(legalCheckBox).click().build().perform();
        Thread.sleep(1000);
    }

    @Override
    public void enterPhone(String phone) throws InterruptedException {
        WebElement legalCheckBox = webDriver.findElement(By.cssSelector("input[class='nsg-form--input has--2-digits']"));
        builder.moveToElement(legalCheckBox).click();
        builder.moveToElement(legalCheckBox).sendKeys(phone);
        Thread.sleep(1000);
    }

    @Override
    public void pressVerify() throws InterruptedException {
        // nsg-button nsg-grad--nike-fuel
        WebElement verifyButton = webDriver.findElement(By.cssSelector("button[type='submit'][class='nsg-button nsg-grad--nike-fuel']"));
        builder.moveToElement(verifyButton).click().build().perform();
        Thread.sleep(5000);
    }

    @Override
    public void enterCode(String code) throws InterruptedException {
        new WebDriverWait(webDriver, 10).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }
}
