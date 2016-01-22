package quoidneuf.dao;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import quoidneuf.entity.Subscriber;

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
			ResultSet rs = st.executeQuery();
			return rs.next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
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
			String query = "SELECT subscriber_id AS _id, first_name, last_name FROM subscriber WHERE login = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, login);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return new Subscriber(rs.getInt("_id"), rs.getString("first_name"), rs.getString("last_name"));
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
			String query = "SELECT subscriber_id FROM subscriber WHERE subscriber_id IN (?)";
			PreparedStatement st = con.prepareStatement(query);
			Array array = con.createArrayOf("INTEGER", userIds.toArray());
			st.setArray(1, array);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return ids;
	}
	

}
