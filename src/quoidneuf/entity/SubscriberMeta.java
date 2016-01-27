package quoidneuf.entity;

public class SubscriberMeta extends Jsonable {

	private int id;
	private String pictureUri;
	private String description;
	private String email;
	private String phone;
	
	public SubscriberMeta() {}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPictureUri() {
		return pictureUri;
	}

	public void setPictureUri(String pictureUri) {
		this.pictureUri = pictureUri;
	}

	public String getDescription() {
		if (description == null) {
			description = "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		if (phone == null) {
			phone = "";
		}
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
