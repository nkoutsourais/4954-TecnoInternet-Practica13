package es.codeurjc.daw.selenium.pages;

import static org.junit.Assert.assertNotNull;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BlogPage extends Page {

    public BlogPage(Page page) {
        super(page);
    }

    public BlogPage(WebDriver driver, int port) {
        super(driver, port);
    }

    public BlogPage get() {
        this.get("/");
        wait.until(ExpectedConditions.elementToBeClickable(By.tagName("h1")));
        return this;
    }

    public NewPostPage newPost() {
        findElementWithText("Nuevo post").click();
        return new NewPostPage(this);
    }

    public PostPage viewPost(String title) {
        findElementWithText(title).click();
        return new PostPage(this);
    }

    public BlogPage checkNewPost(String title) {
        assertNotNull(findElementWithText(title));
        return this;
    }
}