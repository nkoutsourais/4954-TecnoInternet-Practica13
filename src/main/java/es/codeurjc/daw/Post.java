package es.codeurjc.daw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

public class Post {

	interface Basic {
	}

	interface Extended {
	}

	interface Full extends Basic, Extended {
	}

	@JsonView(Basic.class)
	private long id = -1;

	@JsonView(Basic.class)
	private String title;

	@JsonView(Extended.class)
	private String content;

	@JsonView(Extended.class)
	private List<Comment> comments = new ArrayList<>();

	@JsonIgnore
	private Map<Long, Comment> commentsMap = new HashMap<>();

	public Post(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public List<Comment> getComments() {
		return new ArrayList<>(this.commentsMap.values());
	}

	public Comment getComment(long id) {
		return this.commentsMap.get(id);
	}

	public void addComment(Comment comment) {
		this.commentsMap.put(comment.getId(), comment);
	}

	public void deleteComment(long commentId) {
		this.commentsMap.remove(commentId);
	}

	@Override
	public String toString() {
		return this.title + " (" + comments.size() + " comments)";
	}

}
