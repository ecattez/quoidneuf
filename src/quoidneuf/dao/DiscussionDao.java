package quoidneuf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import quoidneuf.entity.Discussion;
import quoidneuf.entity.Message;
import quoidneuf.entity.Subscriber;

/**
 * DAO des discussions.
 */
public class DiscussionDao extends Dao<Integer> {

	public DiscussionDao() {}
	
	@Override
	public boolean exist(Integer id) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM discussion WHERE discussion_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, id);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Vérifie si un utilisateur existe dans une discussion
	 * 
	 * @param	id
	 * 			l'identifiant de la discussion
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 * 
	 * @return	<true> si l'utilisateur existe dans la discussion, <false> sinon
	 */
	public boolean userExistIn(int id, int userId) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM belong_to WHERE discussion_id = ? AND subscriber_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, id);
			st.setInt(2, userId);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Charge une discussion à partir de son id
	 * 
	 * @param	id
	 * 			l'identifiant de la discussion
	 * 
	 * @return	une instance de Discussion, null sinon
	 */
	public Discussion getDiscussion(int id) {
		Discussion discussion = null;
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM subscriber_message_discussion WHERE _to = ? ORDER BY _date ASC LIMIT 100";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if (discussion == null) {
					discussion = new Discussion();
					discussion.setId(rs.getInt("_to"));
					discussion.setName(rs.getString("_name"));
				}
				if (rs.getTimestamp("_date") != null) {
					Message m = new Message();
					Subscriber s = new Subscriber(rs.getInt("_from"), rs.getString("_first_name"), rs.getString("_last_name"));
					m.setSubscriber(s);
					m.setContent(rs.getString("_content"));
					m.setWrittenDate(rs.getTimestamp("_date"));
					discussion.push(m);
				}
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return discussion;
	}

	/**
	 * Charge les discussions d'un utilisateur
	 *
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 * 
	 * @return	une liste des discussions d'un utilisateur
	 */
	public List<Discussion> getDiscussionsOf(int userId) {
		List<Discussion> discussions = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT _to, _name FROM discussion_trie WHERE _to IN (SELECT discussion_id FROM belong_to WHERE subscriber_id = ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				discussions.add(new Discussion(rs.getInt("_to"), rs.getString("_name")));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return discussions;
	}
	
	/**
	 * Charge les abonnés d'une discussion particulière
	 * 
	 * @param	id
	 * 			l'id de la discussion des abonnés
	 * 
	 * @return	la liste des abonnés présents dans une discussion
	 */
	public List<Subscriber> getSubscribersOf(int id) {
		List<Subscriber> subscribers = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id AS _from, first_name AS _first_name, last_name AS _last_name"
					+ " FROM subscriber WHERE subscriber_id IN (SELECT subscriber_id FROM belong_to WHERE discussion_id = ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Subscriber s = new Subscriber();
				s.setId(rs.getInt("_from"));
				s.setFirstName(rs.getString("_first_name"));
				s.setLastName(rs.getString("_last_name"));
				subscribers.add(s);
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return subscribers;
	}
	
	/**
	 * Crée une nouvelle discussion
	 * 
	 * @param	name
	 * 			le nom de la discussion
	 * 
	 * @return	l'id de la discussion crée, -1 sinon
	 */
	public int insertDiscussion(String name) {
		try (Connection con = getConnection()) {
			// Création de la discussion
			String query = "INSERT INTO discussion (discussion_name) VALUES (?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, name);
			st.executeUpdate();
			
			// On récupère l'id de la discussion insérée
			query = "SELECT MAX(discussion_id) AS _to FROM discussion";
			st = con.prepareStatement(query);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getInt(0);
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Retire un utilisateur d'une discussion
	 * 
	 * @param	id
	 * 			l'identifiant de la discussion
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 * 
	 * @return	un entier positif en cas de succès, -1 sinon
	 */
	public int removeUserFrom(int id, int userId) {
		try (Connection con = getConnection()) {
			String query = "DELETE FROM belong_to WHERE subscriber_id = ? AND discussion_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			st.setInt(2, id);
			return st.executeUpdate();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Ajoute un utilisateur à une discussion
	 * 
	 * @param	id
	 * 			l'identifiant de la discussion
	 * @param	usersId
	 * 			l'identifiant de l'utilisateur à ajouter
	 * 
	 * @return	un entier positif en cas de succès, -1 sinon
	 */
	public int insertUserIn(int id, int userId) {
		try (Connection con = getConnection()) {
			String query = "INSERT INTO belong_to VALUES (?,?)";
			PreparedStatement st;
			st = con.prepareStatement(query);
			st.setInt(1, userId);
			st.setInt(2, id);
			return st.executeUpdate();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Ajoute des utilisateurs à une discussion
	 * 
	 * @param	id
	 * 			l'identifiant de la discussion
	 * @param	usersIds
	 * 			l'identifiant de chaque utilisateur à ajouter
	 * 
	 * @return	le nombre d'utilisateurs ajoutés à la discussion
	 */
	public int insertUsersIn(int id, List<Integer> usersIds) {
		int i = 0;
		try (Connection con = getConnection()) {
			String query = "INSERT INTO belong_to VALUES (?,?)";
			PreparedStatement st;
			for (int userId : usersIds) {
				st = con.prepareStatement(query);
				st.setInt(1, userId);
				st.setInt(2, id);
				i += st.executeUpdate();
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		return i;
	}

}
