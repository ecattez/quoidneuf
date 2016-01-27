package quoidneuf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.NamingException;

import quoidneuf.entity.Credential;

import static quoidneuf.entity.Credential.*;

public class CredentialDao extends Dao<String> {
	
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
	public int insert(String login, String password) {
		return insert(login, password, Credential.DEFAULT_ROLE);
	}
	
	/**
	 * 
	 * @param login
	 * @param password
	 * @param role
	 * @return
	 */
	public int insert(String login, String password, String role) {
		try (Connection con = getConnection()) {
			String query = "INSERT INTO credential VALUES (?, ?, ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, login);
			st.setString(2, encode(password));
			st.setString(3, role);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
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
	
	/**
	 * 
	 * @param login
	 * @return
	 */
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
	
	/**
	 * 
	 * @param login
	 * @return
	 */
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
