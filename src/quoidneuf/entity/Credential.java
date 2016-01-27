package quoidneuf.entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Credential extends Jsonable {
	
	public static final int MAX_LOGIN_LENGTH = 16;
	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final String DEFAULT_ROLE = "user";
	
	private String login;
	private String password;
	
	public Credential(String login, String password) {
		this.login = login;
		this.password = password;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Crypte un password en MD5
	 * 
	 * @param	password
	 * 			le mot de passe à crypté
	 * 
	 * @return	le mot de passe crypté
	 */
	public static String encode(String password) {
		byte[] unique = password.getBytes();
		byte[] hash = null;

		try {
			hash = MessageDigest.getInstance("MD5").digest(unique);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		StringBuilder builder = new StringBuilder();
		String hex;
		for (int i=0; i < hash.length; i++) {
			hex = Integer.toHexString(hash[i]);
			if (hex.length() == 1) {
				builder.append('0').append(hex);
			}
			else {
				builder.append(hex.substring(hex.length() - 2));
			}
		}
		return builder.toString();
	}
	
	/**
	 * @return	un mot de passe généré aléatoirement à 8 caractères
	 */
	public static String randomPassword() {
		return encode(String.valueOf(new Random().nextInt())).substring(0, 8);
	}

}
