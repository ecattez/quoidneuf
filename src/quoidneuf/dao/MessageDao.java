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

import org.apache.log4j.Logger;

/**
 * DAO des messages.
 */
public class MessageDao extends Dao<String> {
	
	private static Logger logger = Logger.getLogger(MessageDao.class);

	@Override
	public boolean exist(String id) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM message WHERE message_id = ?";
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
	 * Crée un nouveau message dans une discussion
	 * 
	 * @param	id
	 * 			l'identifiant de la discussion
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 * @param	content
	 * 			le contenu du message
	 * 
	 * @return	un entier positif si le message a bien été créé, -1 sinon
	 */
	public int insertMessage(String id, String discussionId, int userId, String content) {
		try (Connection con = getConnection()) {
			String query = "INSERT INTO message (message_id, subscriber_id, discussion_id, content) VALUES (?, ?, ?, ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, id);
			st.setInt(2, userId);
			st.setString(3, discussionId);
			st.setString(4, content);
			return st.executeUpdate();
		} catch (NamingException | SQLException e) {
			e.printStackTrace();
		}
		logger.error("insertion de la ligne échouée");
		return -1;
	}
	

}
