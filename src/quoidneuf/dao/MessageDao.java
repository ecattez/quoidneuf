package quoidneuf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

public class MessageDao extends Dao<Integer> {

	@Override
	public boolean exist(Integer id) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM message WHERE message_id = ?";
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
	 * Crée un nouveau message dans une discussion
	 * 
	 * @param	id
	 * 			l'identifiant de la discussion
	 * @param	userId
	 * 			l'identifiant de l'utilisateur
	 * @param	content
	 * 			le contenu du message
	 * 
	 * @return	l'id du message créé, -1 sinon
	 */
	public int insertMessage(int id, int userId, String content) {
		try (Connection con = getConnection()) {
			// Création de la discussion
			String query = "INSERT INTO message (subscriber_id, discussion_id) VALUES (?, ?)";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			st.setInt(2, id);
			st.setString(3, content);
			st.executeUpdate();
			
			// On récupère l'id du message inséré
			query = "SELECT MAX(message_id) AS _id FROM message";
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
	

}
