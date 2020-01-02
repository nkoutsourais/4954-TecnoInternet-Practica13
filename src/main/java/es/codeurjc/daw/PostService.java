package es.codeurjc.daw;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class PostService {

	private Map<Long, Post> posts = new ConcurrentHashMap<>();
	private AtomicLong lastPostId = new AtomicLong();
	private AtomicLong lastCommentId = new AtomicLong();

	public Map<Long, Post> getPosts() {
		return this.posts;
	}

	public Post getPost(long id) {
		return this.posts.get(id);
	}

	public void addPost(Post post) {
		post.setId(lastPostId.incrementAndGet());
		this.posts.put(post.getId(), post);
	}

	public void setCommentId(Comment comment) {
		comment.setId(this.lastCommentId.incrementAndGet());
	}

}
