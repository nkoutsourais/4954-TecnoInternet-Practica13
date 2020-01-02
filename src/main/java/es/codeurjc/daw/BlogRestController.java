package es.codeurjc.daw;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping("/api")
public class BlogRestController {

	interface Full extends Post.Full, Comment.Full {
	}

	@Autowired
	private PostService postService;

	@JsonView(Post.Basic.class)
	@GetMapping("/post")
	public ResponseEntity<List<Post>> listPosts() {
		return new ResponseEntity<>(new ArrayList<>(this.postService.getPosts().values()), HttpStatus.OK);
	}

	@JsonView(Full.class)
	@GetMapping("/post/{id}")
	public ResponseEntity<Post> getPost(@PathVariable long id) {
		Post post = this.postService.getPost(id);
		if (post == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(post, HttpStatus.OK);
	}

	@JsonView(Post.Basic.class)
	@PostMapping("/post")
	public ResponseEntity<Post> newPost(@RequestBody Post post) {
		this.postService.addPost(post);
		return new ResponseEntity<>(post, HttpStatus.CREATED);
	}

	@JsonView(Comment.Full.class)
	@PostMapping("/post/{postId}/comment")
	public ResponseEntity<Comment> newComment(@PathVariable long postId, @RequestBody Comment comment) {
		Post post = this.postService.getPost(postId);
		if (post == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		this.postService.setCommentId(comment);
		post.addComment(comment);
		return new ResponseEntity<>(comment, HttpStatus.CREATED);
	}

	@DeleteMapping("/post/{postId}/comment/{commentId}")
	public ResponseEntity<Comment> deleteComment(@PathVariable long postId, @PathVariable long commentId) {
		Post post = this.postService.getPost(postId);
		if (post == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Comment comment = post.getComment(commentId);
		if (comment == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		post.deleteComment(commentId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
