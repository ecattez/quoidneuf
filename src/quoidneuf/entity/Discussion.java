package quoidneuf.entity;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Discussion extends Jsonable {

	private int id;
	private String name;
	private List<Subscriber> subscribers = new LinkedList<>();
	private List<Message> messages = new LinkedList<>();

	public Discussion() {}
	
	public Discussion(String name) {
		this.name = name;
	}
	
	public Discussion(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty("current_subscribers")
	public List<Subscriber> getSubscribers() {
		return subscribers;
	}
	
	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}
	
	public List<Message> getMessages() {
		return messages;
	}
	
	public void push(Message msg) {
		messages.add(msg);
	}
	
	public void push(Subscriber sbr) {
		subscribers.add(sbr);
	}

}
