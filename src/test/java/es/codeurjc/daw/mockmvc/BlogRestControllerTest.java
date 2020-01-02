package es.codeurjc.daw.mockmvc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import es.codeurjc.daw.*;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BlogRestControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private PostService postService;
    
    @Autowired
	ObjectMapper objectMapper;

    @Test
    public void getPosts_shouldReturnListOfPosts() throws Exception {

        Post post = new PostBuilder().getPostWithoutComment();
        Map<Long, Post> posts = new HashMap<>();
        posts.put(Long.parseLong("1"), post);
	 
	    when(postService.getPosts()).thenReturn(posts);

        mvc.perform(get("/api/post")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].title", equalTo(post.getTitle())));
    }

    @Test
    public void getPostById_shouldReturnPost() throws Exception {

        Post post = new PostBuilder().getPostWithoutComment();
	    when(postService.getPost(new Long(isA(Long.class)))).thenReturn(post);

        mvc.perform(get("/api/post/1")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title", equalTo(post.getTitle())))
            .andExpect(jsonPath("$.content", equalTo(post.getContent())));
    }

    @Test
    public void getPostByIdNonValid_shouldReturn404() throws Exception {

        mvc.perform(get("/api/post/100")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void addNewPost_shouldReturn201() throws Exception {

        Post post = new PostBuilder().getPostWithoutComment();
        mvc.perform(post("/api/post")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(post)))
            .andExpect(status().isCreated());
    }

    @Test
    public void addNewComment_shouldReturn201() throws Exception {

        PostBuilder builder = new PostBuilder();
        Post post = builder.getPostWithoutComment();
        when(postService.getPost(new Long(isA(Long.class)))).thenReturn(post);
        doNothing().when(postService).setCommentId(isA(Comment.class));

        assertTrue(post.getComments().isEmpty());
        
        mvc.perform(post("/api/post/1/comment")
			.contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(builder.getComment())))
            .andExpect(status().isCreated());

        assertFalse(post.getComments().isEmpty());
    }

    @Test
    public void deleteComment_shouldReturn204() throws Exception {

        Post post = new PostBuilder().getPostWithOneComment();
        when(postService.getPost(new Long(isA(Long.class)))).thenReturn(post);

        assertFalse(post.getComments().isEmpty());

        mvc.perform(delete("/api/post/1/comment/1"))
            .andExpect(status().isNoContent());

        assertTrue(post.getComments().isEmpty());
    }

    @Test
    public void deleteCommentBadPostId_shouldReturn404() throws Exception {

        mvc.perform(delete("/api/post/1/comment/1"))
		    .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCommentBadCommentId_shouldReturn404() throws Exception {

        when(postService.getPost(new Long(isA(Long.class)))).thenReturn(new PostBuilder().getPostWithoutComment());

        mvc.perform(delete("/api/post/1/comment/2"))
		    .andExpect(status().isNotFound());
    }
}