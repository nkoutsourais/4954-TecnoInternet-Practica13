package es.codeurjc.daw.selenium.pages;

import static java.lang.String.format;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class Page {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected int port;

    public Page(WebDriver driver, int port) {
        this.driver = driver;
        this.port = port;
        this.wait = new WebDriverWait(driver, 10);
    }

    public Page(Page page) {
        this.driver = page.driver;
        this.port = page.port;
        this.wait = new WebDriverWait(driver, 10);
    }

    protected void get(String path) {
        driver.get("localhost:" + this.port + path);
    }

    protected boolean isElementPresent(String text) {
        try {
            findElementWithText(text);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    protected WebElement findElementWithText(String text) {
        return driver.findElement(getConditionForText(text));
    }

    protected List<WebElement> findElementsWithText(String text) {
        return driver.findElements(getConditionForText(text));
    }

    protected By getConditionForText(String text) {
        return By.xpath(format("//*[text()='%s']", text));
    }
}