package quoidneuf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import quoidneuf.entity.Subscriber;

/**
 * DAO autour des relations amicales.
 */
public class FriendDao extends Dao<Integer> {

	@Override
	public boolean exist(Integer id) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM friend_with WHERE friend_1 = ? or friend_2 = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, id);
			st.setInt(2, id);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/* Trie deux identifiants dans l'ordre croissant */
	private int[] sort(int userId_1, int userId_2) {
		int left = userId_1;
		int right = userId_2;
		
		if (userId_1 > userId_2) {
			left = userId_2;
			right = userId_1;
		}
		return new int[] { left, right };
	}
	
	/* Crée un lien d'amitié */
	private int insertFriends(int userId_1, int userId_2, boolean status) {
		int[] sort = sort(userId_1, userId_2);
		try (Connection con = getConnection()) {
			String query = "INSERT INTO friend_with VALUES (?,?,?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, sort[0]);
			st.setInt(2, sort[1]);
			st.setBoolean(3, status);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/* Met à jour un lien d'amitié */
	private int updateStatus(int userId_1, int userId_2, boolean status) {
		int[] sort = sort(userId_1, userId_2);
		try (Connection con = getConnection()) {
			String query = "UPDATE friend_with SET status = ? WHERE friend_1 = ? AND friend_2 = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setBoolean(1, status);
			st.setInt(2, sort[0]);
			st.setInt(3, sort[1]);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/* Supprime un lien d'amitié */
	public int removeFriendship(int userId_1, int userId_2) {
		int[] sort = sort(userId_1, userId_2);
		try (Connection con = getConnection()) {
			String query = "DELETE FROM friend_with WHERE friend_1 = ? AND friend_2 = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, sort[0]);
			st.setInt(2, sort[1]);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * Récupère la liste des utilisateurs liés à un utilisateur
	 *  
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 * @param	status
	 * 			le status de la liaison (demande d'ajout / amis)
	 */
	public List<Subscriber> getLinkedWith(int userId, boolean status) {
		List<Subscriber> friends = new ArrayList<Subscriber>();
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id AS id, first_name, last_name FROM friend_with INNER JOIN subscriber"
					+ " ON friend_2 = subscriber_id AND friend_1 = ?"
					+ " WHERE status = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			st.setBoolean(2, status);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				friends.add(new Subscriber(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name")));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return friends;
	}
	
	/**
	 * Vérifie le status d'un lien d'amitié entre deux utilisateurs
	 * 
	 * @param	userId_1
	 * 			l'identifiant du premier utilisateur
	 * @param	userId_2
	 * 			l'identifiant du second utilisateur
	 * @param	status
	 * 			le status d'amitié (demande d'ajout / amis)
	 * 
	 * @return	<true> si les deux utilisateurs sont liés par le status en paramètre, <false> sinon
	 */
	public boolean hasStatus(int userId_1, int userId_2, boolean status) {
		int[] sort = sort(userId_1, userId_2);
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM friend_with WHERE friend_1 = ? AND friend_2 = ? AND status = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, sort[0]);
			st.setInt(2, sort[1]);
			st.setBoolean(3, status);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Vérifie si deux utilisateurs sont liés
	 * 
	 * @param	userId_1
	 * 			l'identifiant du premier utilisateur
	 * @param	userId_2
	 * 			l'identifiant du second utilisateur
	 * 
	 * @return	<true> si deux utilisateurs ont un lien d'amitié, <false> sinon
	 */
	public boolean areLinked(int userId_1, int userId_2) {
		int[] sort = sort(userId_1, userId_2);
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM friend_with WHERE friend_1 = ? AND friend_2 = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, sort[0]);
			st.setInt(2, sort[1]);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Crée une demande d'ajout entre deux utilisateurs
	 * 
	 * @param	userId_1
	 * 			l'identifiant du premier utilisateur
	 * @param	userId_2
	 * 			l'identifiant du second utilisateur
	 * 
	 * @return	<true> si la création s'est effectuée, <false> sinon
	 */
	public int requestForFriendship(int userId_1, int userId_2) {
		return insertFriends(userId_1, userId_2, false);
	}
	
	/**
	 * Répond à une demande d'ajout entre deux utilisateurs
	 * 
	 * @param	userId_1
	 * 			l'identifiant du premier utilisateur
	 * @param	userId_2
	 * 			l'identifiant du second utilisateur
	 * 
	 * @return	<true> si la confirmation a lieu, <false> sinon
	 */
	public int answerToRequest(int userId_1, int userId_2) {
		return updateStatus(userId_1, userId_2, true);
	}
	
	/**
	 * Vérifie si deux utilisateurs sont amis
	 * 
	 * @param	userId_1
	 * 			l'identifiant du premier utilisateur
	 * @param	userId_2
	 * 			l'identifiant du second utilisateur
	 * 
	 * @return	<true> s'ils sont amis, <false> sinon
	 */
	public boolean areFriends(int userId_1, int userId_2) {
		return hasStatus(userId_1, userId_2, true);
	}
	
	/**
	 * Vérifie si une demande d'ajout existe entre deux utilisateurs
	 * 
	 * @param	userId_1
	 * 			l'identifiant du premier utilisateur
	 * @param	userId_2
	 * 			l'identifiant du second utilisateur
	 * 
	 * @return	<true> si une demande d'amis existe, <false> sinon
	 */
	public boolean requestedForFriendship(int userId_1, int userId_2) {
		return hasStatus(userId_1, userId_2, false);
	}
	
	/**
	 * Récupère la liste des utilisateurs qui doivent confirmer la demande d'ajout
	 * de l'utilisateur passé en paramètre
	 * 
	 * @param	userId
	 * 			l'identifiant de l'utilisateur principal
	 * 
	 * @return	la liste des futurs amis de l'utilisateur
	 */
	public List<Subscriber> getRequestsOf(int userId) {
		return getLinkedWith(userId, false);
	}
	
	/**
	 * Récupère la liste des amis de l'utilisateur passé en paramètre
	 * 
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 * 
	 * @return	la liste des amis de l'utilisateur
	 */
	public List<Subscriber> getFriendsOf(int userId) {
		return getLinkedWith(userId, true);
	}
	

}
