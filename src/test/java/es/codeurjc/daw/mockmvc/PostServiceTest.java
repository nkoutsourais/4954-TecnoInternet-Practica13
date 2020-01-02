package es.codeurjc.daw.mockmvc;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import es.codeurjc.daw.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PostServiceTest {

    static PostService postService;

    @BeforeAll
    public static void setUp() {
        postService = new PostService();
        postService.addPost(new Post("post1", "post1"));
        postService.addPost(new Post("post2", "post2"));
    }

    @Test
	public void getPost_shouldReturnListOfPost() throws Exception {
	    Map<Long, Post> posts = postService.getPosts();
	    assertThat(posts).hasSize(2);
        assertThat(((Post)posts.values().toArray()[0]).getTitle()).isEqualTo("post1");
    }
    
    @Test
	public void addPostAndget_shouldReturnSamePost() throws Exception {
        Post post3 = new Post("post3", "post3");
        postService.addPost(post3);
        Post postGet = postService.getPost(post3.getId());
	    assertEquals(post3, postGet);
        assertThat(post3.getTitle()).isEqualTo(postGet.getTitle());
        assertThat(post3.getContent()).isEqualTo(postGet.getContent());
    }
    
    @Test
	public void setCommentId_shouldReturnIdGreaterZero() throws Exception {
	    Comment comment = new Comment("author", "message");
        assertTrue(comment.getId() <= 0);
        postService.setCommentId(comment);
        assertTrue(comment.getId() > 0);
    }
}