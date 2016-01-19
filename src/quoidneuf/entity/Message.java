package quoidneuf.entity;

import java.sql.Timestamp;

/**
 * Représentation d'un message écrit par un utilisateur.
 */
public class Message extends Jsonable {
	
	private Subscriber subscriber;
	private String content;
	private Timestamp writtenDate;
	
	public Message() {}
	
	public Message(Subscriber subscriber, String content, Timestamp writtenDate) {
		this.subscriber = subscriber;
		this.content = content;
		this.writtenDate = writtenDate;
	}
	
	public Subscriber getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getWrittenDate() {
		return writtenDate;
	}

	public void setWrittenDate(Timestamp writtenDate) {
		this.writtenDate = writtenDate;
	}

}
