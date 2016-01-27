package quoidneuf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import quoidneuf.entity.Subscriber;
import quoidneuf.util.Matcher;

/**
 * DAO des utilisateurs.
 */
public class SubscriberDao extends Dao<Integer> {

	@Override
	public boolean exist(Integer id) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM subscriber WHERE subscriber_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, id);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean exist(String login, String email) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM subscriber WHERE login = ? AND subscriber_id = (SELECT subscriber_id FROM subscriber_meta WHERE email = ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, login);
			st.setString(2, email);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Récupère un abonné à partir de son identifiant d'utilisateur
	 * 
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 * 
	 * @return	une instance de Subscriber, null sinon
	 */
	public Subscriber getById(int userId) {
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id AS id, first_name, last_name FROM subscriber WHERE subscriber_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return new Subscriber(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Récupère un abonné à partir de ses identifiants d'authentification
	 * 
	 * @param	login
	 * 			le login de l'utilisateur
	 * 
	 * @return	une instance de Subscriber, null sinon
	 */
	public Subscriber getByLogin(String login) {
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id AS id, first_name, last_name FROM subscriber WHERE login = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, login);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return new Subscriber(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Récupère l'identifiant d'un utilisateur via son login
	 * 
	 * @param	login
	 * 			le login de l'utilisateur
	 * 
	 * @return	l'identifiant de l'utilisateur, -1 sinon
	 */
	public int getIdByLogin(String login) {
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id FROM subscriber WHERE login = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, login);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Liste les identifiants des utilisateurs qui sont corrects
	 * 
	 * @param	userIds
	 * 			les identifiants utilisateurs
	 * 
	 * @return	la liste de tous les bons identifiants utilisateurs
	 */
	public List<Integer> correctIds(List<Integer> userIds) {
		List<Integer> ids = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id FROM subscriber WHERE subscriber_id IN";
			String s = "";
			int size = userIds.size();
			for (int i=0; i < size; i++) {
				s += "?";
				if (i < size - 1) {
					s += ",";
				}
			}
			query += "(" + s + ")";
			PreparedStatement st = con.prepareStatement(query);
			for (int i=1; i <= size; i++) {
				st.setInt(i, userIds.get(i-1));
			}
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return ids;
	}
	
	public int insert(String login, String firstname, String lastname, String birthday) {
		try (Connection con = getConnection()) {
			String query = "INSERT INTO subscriber(login, first_name, last_name, birthdate) VALUES (?, ?, ?, ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, login);
			st.setString(2, firstname);
			st.setString(3, lastname);
			st.setDate(4, new java.sql.Date(Matcher.convertDate(birthday).getTime()));
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Supprime un utilisateur de la base
	 * 
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 * 
	 * @return	un entier positif si la suppression a fonctionnée
	 */
	public int remove(int userId) {
		try (Connection con = getConnection()) {
			String query = "DELETE FROM credential WHERE login = (SELECT login FROM subscriber WHERE subscriber_id = ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
	}

}
