package quoidneuf.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import javax.naming.NamingException;

public class CredentialDao extends Dao<String> {

	/* Hash un mot de passe en clair avec MD5 */
	private String encode(String password) {
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
	private String randomPassword() {
		return encode(String.valueOf(new Random().nextInt())).substring(0, 8);
	}
	
	@Override
	public boolean exist(String id) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM credential WHERE login = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, id);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	public int changePassword(String login, String password) {
		try (Connection con = getConnection()) {
			String query = "UPDATE credential SET password_hash = ? WHERE login = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, encode(password));
			st.setString(2, login);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
		
	}
	
	public String resetPassword(String login) {
		try (Connection con = getConnection()) {
			String query = "UPDATE credential SET password_hash = ? WHERE login = ?";
			PreparedStatement st = con.prepareStatement(query);
			String password = randomPassword();
			st.setString(1, encode(password));
			st.setString(2, login);
			if (st.executeUpdate() > 0) {
				return password;
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public int remove(String login) {
		try (Connection con = getConnection()) {
			String query = "DELETE FROM credential WHERE login = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, login);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
