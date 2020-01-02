package es.codeurjc.daw;

import com.fasterxml.jackson.annotation.JsonView;

public class Comment {

	interface Full {
	}

	@JsonView(Full.class)
	private long id = -1;

	@JsonView(Full.class)
	private String author;

	@JsonView(Full.class)
	private String message;

	public Comment(String author, String message) {
		this.author = author;
		this.message = message;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return this.author + ": " + this.message;
	}

}
