package quoidneuf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.NamingException;

public class MessageDao extends Dao<String> {

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
		return -1;
	}
	

}
