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
	
	/**
	 * Vérifie si un utilisateur existe avec un certain login et un certain email
	 * 
	 * @param	login
	 * 			le login de l'utilisateur
	 * @param	email
	 * 			l'email de l'utilisateur
	 * 
	 * @return	<true> si l'utilisateur existe, <false> sinon
	 */
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
			String query = "SELECT subscriber_id AS id, first_name, last_name, birthday FROM subscriber WHERE subscriber_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return new Subscriber(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("birthday"));
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
			String query = "SELECT subscriber_id AS id, first_name, last_name, birthday FROM subscriber WHERE login = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, login);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				return new Subscriber(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("birthday"));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Récupère un ou plusieurs abonnés à partir de critères de recherche
	 * 
	 * @param	firstname
	 * 			le prénom approximatif de l'utilisateur
	 * @param	lastname
	 * 			le nom de famille approximatif de l'utilisateur
	 * @param	email
	 * 			l'email approximatif de l'utilisateur
	 * 
	 * @return	une liste des Subscriber répondant à la recherche, la liste vide sinon
	 */
	public List<Subscriber> search(String firstname, String lastname, String email) {
		List<Subscriber> subscribers = new ArrayList<>();
		try (Connection con = getConnection()) {
			String query = "SELECT subscriber_id AS id, first_name, last_name, birthday FROM subscriber INNER JOIN subscriber_meta USING(subscriber_id)"
					+ " WHERE first_name LIKE ? OR last_name LIKE ? OR email LIKE ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, "%" + firstname + "%");
			st.setString(2, "%" + lastname + "%");
			st.setString(3, "%" + email + "%");
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				subscribers.add(new Subscriber(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("birthday")));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return subscribers;
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
	
	/**
	 * Ajoute un utilisateur dans la base de données
	 * 
	 * @param	login
	 * 			le login de l'utilisateur
	 * @param	firstname
	 * 			le prénom de l'utilisateur
	 * @param	lastname
	 * 			le nom de l'utilisateur
	 * @param	birthday
	 * 			la date d'anniversaire de l'utilisateur
	 * 
	 * @return	un entier positif en cas de succès, -1 sinon
	 */
	public int insert(String login, String firstname, String lastname, String birthday) {
		try (Connection con = getConnection()) {
			String query = "INSERT INTO subscriber(login, first_name, last_name, birthday) VALUES (?, ?, ?, ?)";
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
