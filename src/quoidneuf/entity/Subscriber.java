package quoidneuf.entity;

/**
 * Représentation d'un utilisateur de l'application
 */
public class Subscriber extends Jsonable {
	
	private int id;
	private String firstName;
	private String lastName;
	private SubscriberMeta meta;
	
	public Subscriber() {}
	
	public Subscriber(int id, String firstName, String lastName) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public SubscriberMeta getMeta() {
		return meta;
	}
	
	public void setMeta(SubscriberMeta meta) {
		this.meta = meta;
	}

}
