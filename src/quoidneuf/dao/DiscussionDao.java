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

import quoidneuf.entity.Discussion;
import quoidneuf.entity.Message;
import quoidneuf.entity.Subscriber;

/**
 * DAO des discussions.
 */
public class DiscussionDao extends Dao<String> {
	
	private static Logger logger = Logger.getLogger(DiscussionDao.class);

	public DiscussionDao() {}
	
	@Override
	public boolean exist(String id) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM discussion WHERE discussion_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, id);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		logger.error(id + " n'existe pas");
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
	public boolean userExistIn(String id, int userId) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM belong_to WHERE discussion_id = ? AND subscriber_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, id);
			st.setInt(2, userId);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		logger.error("l'utilisateur " + userId +  "n'existe pas dans la discussion " + id);
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
	public Discussion getDiscussion(String id) {
		Discussion discussion = null;
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM subscriber_message_discussion WHERE _to = ? LIMIT 100";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				if (discussion == null) {
					discussion = new Discussion(rs.getString("_to"), rs.getString("_name"));
				}
				if (rs.getTimestamp("_date") != null) {
					Message m = new Message();
					Subscriber s = new Subscriber();
					s.setId(rs.getInt("_from"));
					s.setFirstName(rs.getString("_first_name"));
					s.setLastName(rs.getString("_last_name"));
					m.setOwner(s);
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
				discussions.add(new Discussion(rs.getString("_to"), rs.getString("_name")));
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
	public List<Subscriber> getSubscribersOf(String id) {
		List<Subscriber> subscribers = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id AS _from, first_name, last_name"
					+ " FROM subscriber WHERE subscriber_id IN (SELECT subscriber_id FROM belong_to WHERE discussion_id = ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Subscriber s = new Subscriber();
				s.setId(rs.getInt("_from"));
				s.setFirstName(rs.getString("first_name"));
				s.setLastName(rs.getString("last_name"));
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
	 * @return	un entier positif si la discussion a bien été créée, -1 sinon
	 */
	public int insertDiscussion(String id, String name) {
		try (Connection con = getConnection()) {
			String query = "INSERT INTO discussion (discussion_id, discussion_name) VALUES (?, ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, id);
			st.setString(2, name);
			return st.executeUpdate();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		logger.error("insertion de la discussion " + id + " échouée");
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
	public int removeUserFrom(String id, int userId) {
		try (Connection con = getConnection()) {
			String query = "DELETE FROM belong_to WHERE subscriber_id = ? AND discussion_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			st.setString(2, id);
			return st.executeUpdate();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		logger.error("suppression de " + userId + " dans la discussion " + id + " échouée");
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
	public int insertUserIn(String id, int userId) {
		try (Connection con = getConnection()) {
			String query = "INSERT INTO belong_to VALUES (?,?)";
			PreparedStatement st;
			st = con.prepareStatement(query);
			st.setInt(1, userId);
			st.setString(2, id);
			return st.executeUpdate();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		logger.error("insertion de " + userId + " dans la discussion " + id + " échouée");
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
	public int insertUsersIn(String id, List<Integer> usersIds) {
		int i = 0;
		try (Connection con = getConnection()) {
			String query = "INSERT INTO belong_to VALUES (?,?)";
			PreparedStatement st;
			for (int userId : usersIds) {
				if (userExistIn(id, userId)) {
					logger.warn(userId + " existe déjà dans la discussion, insertion annulée");
					continue;
				}
				st = con.prepareStatement(query);
				try {
					st.setInt(1, userId);
					st.setString(2, id);
					i += st.executeUpdate();
				} catch (SQLException e) {
					logger.error("insertion de " + userId + " dans la discussion " + id + " échouée");
					e.printStackTrace();
				}
			}
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		logger.info("insertion réussie");
		return i;
	}

}
