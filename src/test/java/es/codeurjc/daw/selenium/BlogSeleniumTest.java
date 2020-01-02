package es.codeurjc.daw.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.web.server.LocalServerPort;

import es.codeurjc.daw.Application;
import es.codeurjc.daw.selenium.pages.*;

import org.springframework.boot.test.context.SpringBootTest;

import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogSeleniumTest {

    @LocalServerPort
    int port;

	WebDriver driver;
	
	@BeforeAll
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}
	
	@BeforeEach
	public void setup() {
		driver = new ChromeDriver();
	}
	
	@AfterEach
	public void teardown() {
		if(driver != null) {
			driver.quit();
		}
    }
    
    @Test
	public void createPost() throws InterruptedException {
		BlogPage blog = new BlogPage(driver, port);
		
		String title = "titulo1";
		String content = "descripcion1";
		
		blog.get()
			.newPost()
				.fillNewPost(title, content)
			.sendPost()
			.returnBlog()
				.checkNewPost(title);
    }
    
    @Test
	public void createCommentAndDelete() throws InterruptedException {
		BlogPage blog = new BlogPage(driver, port);
		
		String title = "titulo1";
        String content = "descripcion1";
        String author = "author1";
        String message = "message1";
		
		blog.get()
			.newPost()
				.fillNewPost(title, content)
                .sendPost()
            .fillNewComment(author, message)
                .sendComment()
            .checkNewComment(author, message)
                .delComment()
            .checkDeleteComment(author, message);
	}
}