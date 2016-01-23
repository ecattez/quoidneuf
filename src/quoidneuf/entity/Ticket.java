package quoidneuf.entity;

/**
 * Un Ticket est principalement généré lorsqu'une erreur se produit.
 */
public class Ticket extends Jsonable {

	private int code;
	private String message;
	
	public Ticket() {}
	
	public Ticket(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
