package es.codeurjc.daw.restassured;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogRestControllerTest {

    @LocalServerPort
    int port;
	
	@BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
	public void getPosts_shouldReturnListOfPosts() {
		
		//Given
		int id1 = createPostAndReturnId("titulo1", "descripcion1");
		int id2 = createPostAndReturnId("titulo2", "descripcion2");
			
		//When
		when()
			.get("/api/post/").
		
		//Then
		then()
			.statusCode(200).
			body(
				"id", hasItems(id1, id2),
                "title", hasItems("titulo1","titulo2")
            );
    }
    
    @Test
	public void getPostById_shouldReturnPost() {
        
        String title = "titulo1";
        String content = "descripcion1";
		//Given
		int idPost = createPostAndReturnId(title, content);
			
		//When
		when()
			.get("/api/post/{id}", idPost).
		
		//Then
		then()
			.statusCode(200).
			body(
				"id", equalTo(idPost),
				"title", equalTo(title),
				"content",equalTo(content));
    }
    
    @Test
	public void getPostByIdNonValid_shouldReturn404() {
		
		//When
		when()
			.get("/api/post/{id}", 100).
		
		//Then
		then()
			.statusCode(404);
    }

    @Test
    public void addNewPost_shouldReturn201()  {
        String title = "titulo1";
        //Given
        given().
			contentType("application/json").
            body("{\"title\":\"" + title + "\", \"content\":\"descripcion1\" }").
            
        //When
		when().
            post("/api/post/").
            
        //Then
		then().
			statusCode(201).
			body(
                "$", hasKey("id"),
                "title", equalTo(title)
            );
    }

    @Test
    public void addNewComment_shouldReturn201() {
        //Given
		int idPost = createPostAndReturnId("test1", "test1");

        //When
        given().
			contentType("application/json").
			body("{\"author\":\"autor1\", \"message\":\"message1\" }").
		when().
			post("/api/post/{id}/comment", idPost).
		then().
			statusCode(201).
			body(
                "$", hasKey("id"),
                "author", equalTo("autor1")
            );

        //then
		when()
            .get("/api/post/{id}", idPost)
            .then()
                .statusCode(200)
                .body(
                    "comments.author", hasItems(containsString("autor1"))
                );
    }

    @Test
    public void deleteComment_shouldReturn204() {
        //Given
		int idPost = createPostAndReturnId("test1", "test1");

        Response response = given().
            contentType("application/json").
            body("{\"author\":\"autor1\", \"message\":\"message1\" }").
        when().
            post("/api/post/{id}/comment", idPost)
        .andReturn();

        int idComment = from(response.getBody().asString()).get("id");

        //When
		when().
			delete("/api/post/{idPost}/comment/{idComment}", idPost, idComment).
		then().
			statusCode(204);

        //Then
        when()
            .get("/api/post/{id}", idPost).
        then()
            .statusCode(200).
        body(
            "$", not(hasItem("comments"))
        );
    }

    @Test
    public void deleteCommentBadPostId_shouldReturn404() {
		when().
            delete("/api/post/{idPost}/comment/{idComment}", 100, 100).
        then().
            statusCode(404);
    }

    @Test
    public void deleteCommentBadCommentId_shouldReturn404() {
        //Given
        int idPost = createPostAndReturnId("test1", "test1");

        //When
		when().
            delete("/api/post/{idPost}/comment/{idComment}", idPost, 100).

        //Then
        then().
            statusCode(404);
    }

    private int createPostAndReturnId(String title, String content) {
        Response response = given().
            contentType("application/json").
            body("{\"title\":\"" + title + "\", \"content\":\"" + content + "\" }").
        when().
            post("/api/post").andReturn();

        return from(response.getBody().asString()).get("id");
    }
}