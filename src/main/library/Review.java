package main.library;

import org.msgpack.annotation.Message;

@Message
public class Review {
	private int id;
	private int rating;
	private String comment;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	//@JsonProperty("rating")
	public int getRating() {
		return rating;
	}
	
	//@JsonProperty("rating")
	public void setRating(int rating) {
		this.rating = rating;
	}
	
	//@JsonProperty("comment")
	public String getComment() {
		return comment;
	}
	
	//@JsonProperty("comment")
	public void setComment(String comment) {
		this.comment = comment;
	}
}
