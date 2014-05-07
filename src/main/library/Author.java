package main.library;

import org.msgpack.annotation.Message;

@Message
public class Author {
	private int id;
	//@NotNull
	//@NotEmpty
	private String name;

	//@JsonProperty("name")
	public String getName() {
		return name;
	}

	//@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
