/**
 * This file is part of quoidneuf.
 *
 * quoidneuf is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * quoidneuf is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.				 
 * 
 * You should have received a copy of the GNU General Public License
 * along with quoidneuf.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Edouard CATTEZ <edouard.cattez@sfr.fr> (La 7 Production)
 */
package quoidneuf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import quoidneuf.entity.Subscriber;

/**
 * DAO autour des relations amicales.
 */
public class FriendDao extends Dao<Integer> {
	
	private static Logger logger = Logger.getLogger(FriendDao.class);
	
	public static final int ARE_FRIENDS = 0;
	
	@Override
	public boolean exist(Integer id) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM friend_with WHERE friend_1 = ? OR friend_2 = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, id);
			st.setInt(2, id);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		logger.error(id + " n'existe pas");
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
	private int insertFriends(int userId_1, int userId_2, int status) {
		int[] sort = sort(userId_1, userId_2);
		try (Connection con = getConnection()) {
			String query = "INSERT INTO friend_with VALUES (?,?,?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, sort[0]);
			st.setInt(2, sort[1]);
			st.setInt(3, status);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		logger.error("insertion de la ligne échouée");
		return -1;
	}
	
	/* Met à jour un lien d'amitié */
	private int createFriendship(int userId_1, int userId_2, int status) {
		int[] sort = sort(userId_1, userId_2);
		try (Connection con = getConnection()) {
			String query = "UPDATE friend_with SET status = ? WHERE friend_1 = ? AND friend_2 = ? AND status = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, ARE_FRIENDS);
			st.setInt(2, sort[0]);
			st.setInt(3, sort[1]);
			st.setInt(4, status);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		logger.error("création de l'amitié échouée");
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
		logger.error("suppression de la ligne échouée");
		return -1;
	}
	
	private List<Subscriber> getFriendsByStatus(int userId, int status) {
		List<Subscriber> friends = new ArrayList<Subscriber>();
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id AS id, first_name, last_name, birthday FROM friend_with, subscriber"
					+ " WHERE ((friend_1 = ? AND friend_2 = subscriber_id) OR (friend_2 = ? AND friend_1 = subscriber_id)) AND status = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			st.setInt(2, userId);
			st.setInt(3, status);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				friends.add(new Subscriber(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("birthday")));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return friends;
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
	public List<Subscriber> getRequestsFrom(int userId) {
		return getFriendsByStatus(userId, userId);
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
	public List<Subscriber> getRequestsTo(int userId) {
		List<Subscriber> friends = new ArrayList<Subscriber>();
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id AS id, first_name, last_name, birthday FROM friend_with, subscriber"
					+ " WHERE ((friend_1 = ? AND friend_2 = subscriber_id) OR (friend_2 = ? AND friend_1 = subscriber_id)) AND status = subscriber_id";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			st.setInt(2, userId);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				friends.add(new Subscriber(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("birthday")));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return friends;
	}
	
	/**
	 * Récupère la liste des utilisateurs amis à un utilisateur
	 *  
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 */
	public List<Subscriber> getFriendsOf(int userId) {
		return getFriendsByStatus(userId, ARE_FRIENDS);
	}
	
	/**
	 * Récupère le status d'amitié entre deux utilisateurs
	 * 
	 * @param	userId_1
	 * 			l'identifiant du premier utilisateur
	 * @param	userId_2
	 * 			l'identifiant du second utilisateur
	 * 
	 * @return	le status d'amitié entre deux utilisateurs, -1 sinon
	 */
	public int getStatus(int userId_1, int userId_2) {
		int[] sort = sort(userId_1, userId_2);
		try (Connection con = getConnection()) {
			String query = "SELECT status FROM friend_with WHERE friend_1 = ? AND friend_2 = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, sort[0]);
			st.setInt(2, sort[1]);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return rs.getInt("status");
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		logger.warn("aucun status trouvé");
		return -1;
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
		logger.warn("aucune liaison trouvée");
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
		return insertFriends(userId_1, userId_2, userId_1);
	}
	
	/**
	 * Répond à une demande d'ajout entre deux utilisateurs
	 * 
	 * @param	userId_1
	 * 			l'identifiant de l'utilisateur qui accepte l'ajout
	 * @param	userId_2
	 * 			l'identifiant de l'utilisateur qui souhaitait etre ami
	 * 
	 * @return	<true> si la confirmation a lieu, <false> sinon
	 */
	public int answerToRequest(int userId_1, int userId_2) {
		return createFriendship(userId_1, userId_2, userId_2);
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
		return getStatus(userId_1, userId_2) == ARE_FRIENDS;
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
		return getStatus(userId_1, userId_2) != ARE_FRIENDS;
	}

}
