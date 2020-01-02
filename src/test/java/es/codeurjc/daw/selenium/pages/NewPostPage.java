package es.codeurjc.daw.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class NewPostPage extends Page {

    public NewPostPage(Page page) {
        super(page);
    }

    public NewPostPage(WebDriver driver, int port) {
        super(driver, port);
    }

    public NewPostPage get() {
        this.get("/post/new");
        wait.until(ExpectedConditions.elementToBeClickable(By.tagName("h1")));
        return this;
    }

    public NewPostPage fillNewPost(String title, String content) {
        driver.findElement(By.name("title")).sendKeys(title);
        driver.findElement(By.name("content")).sendKeys(content);
        return this;
    }

    public PostPage sendPost() {
        driver.findElement(By.xpath("//input[@type='submit']")).click();
        return new PostPage(this);
    }
}