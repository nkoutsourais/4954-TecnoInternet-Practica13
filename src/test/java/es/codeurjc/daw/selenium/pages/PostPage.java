package es.codeurjc.daw.selenium.pages;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PostPage extends Page {

    public PostPage(Page page) {
        super(page);
    }

    public PostPage(WebDriver driver, int port) {
        super(driver, port);
    }

    public PostPage get(Long id) {
        this.get("/post/" + id);
        wait.until(ExpectedConditions.elementToBeClickable(By.tagName("h1")));
        return this;
    }

    public PostPage checkPost(String title, String content) {
    	assertNotNull(findElementWithText(title));
    	assertNotNull(findElementWithText(content));
        return this;
    }
    
    public BlogPage returnBlog() {
    	findElementWithText(" Atrás ").click();
        return new BlogPage(this);
    }

    public PostPage fillNewComment(String author, String message) {
        driver.findElement(By.name("author")).sendKeys(author);
        driver.findElement(By.name("message")).sendKeys(message);
        return this;
    }

    public PostPage sendComment() {
        driver.findElement(By.xpath("//input[@value='Enviar']")).click();
        return new PostPage(this);
    }

    public PostPage delComment() {
        driver.findElement(By.xpath("//input[@value='Borrar']")).click();
        return new PostPage(this);
    }

    public PostPage checkNewComment(String author, String message) {
        assertTrue(isElementPresent(author + ": " + message));
        return this;
    }
    
    public PostPage checkDeleteComment(String author, String message) {
        assertFalse(isElementPresent(author + ": " + message));
        return this;
    }
}