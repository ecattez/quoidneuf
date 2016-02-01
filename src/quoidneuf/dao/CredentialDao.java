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
import java.sql.SQLException;

import javax.naming.NamingException;

import quoidneuf.entity.Credential;

import static quoidneuf.entity.Credential.*;

/**
 * DAO de l'authentification.
 */
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
	 * Ajoute les identifiants de l'utilisateur dans la base de données
	 * 
	 * @param	login
	 * 			le login de l'utiliateur
	 * @param	password
	 * 			le mot de passe de l'utilisateur
	 * 
	 * @return	un entier positif en cas de succès, -1 sinon
	 */
	public int insert(String login, String password) {
		return insert(login, password, Credential.DEFAULT_ROLE);
	}
	
	/**
	 * Ajoute les identifiants de l'utilisateur dans la base de données
	 * 
	 * @param	login
	 * 			le login de l'utilisateur
	 * @param	password
	 * 			le mot de passe de l'utilisateur
	 * @param	role
	 * 			le rôle de l'utilisateur
	 * 
	 * @return	un entier positif en cas de succès, -1 sinon
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
	 * Modifie le mot de passe d'un utilisateur
	 * 
	 * @param	login
	 * 			le login de l'utilisateur
	 * @param	password
	 * 			le nouveau mot de passe de l'utilisateur
	 * 
	 * @return	un entier positif en cas de succès, -1 sinon
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
	 * Réinitialise le mot de passe d'un utilisateur
	 * 
	 * @param	login
	 * 			le login de l'utilisateur
	 * 
	 * @return	le nouveau mot de passe en cas de succès, la chaîne vide sinon
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
	 * Supprime les identifiants d'un utilisateur
	 * 
	 * @param	login
	 * 			le login de l'utilisateur
	 * 
	 * @return	un entier positif en cas de succès, -1 sinon
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
