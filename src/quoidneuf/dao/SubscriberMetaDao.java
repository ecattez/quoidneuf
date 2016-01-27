package quoidneuf.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import quoidneuf.entity.SubscriberMeta;

public class SubscriberMetaDao extends Dao<Integer> {

	@Override
	public boolean exist(Integer id) {
		try (Connection con = getConnection()) {
			String query = "SELECT 1 FROM subscriber_meta WHERE subscriber_meta_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, id);
			return st.executeQuery().next();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public SubscriberMeta fromSubscriber(int userId) {
		SubscriberMeta meta = null;
		try (Connection con = getConnection()) {
			String query = "SELECT * FROM subscriber_meta WHERE subscriber_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setInt(1, userId);
			ResultSet rs = st.executeQuery();
			if (rs.next()) {				
				meta = new SubscriberMeta();
				meta.setId(rs.getInt("subscriber_meta_id"));
				meta.setDescription(rs.getString("description"));
				meta.setEmail(rs.getString("email"));
				meta.setPhone(rs.getString("phone"));
				meta.setPictureUri(rs.getString("picture"));
			}
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return meta;
	}
	
	public int updateFromSubscriber(int userId, SubscriberMeta meta) {		
		try (Connection con = getConnection()) {
			String query = "UPDATE subscriber_meta SET picture = ?, description = ?, email = ?, phone = ? WHERE subscriber_id = ?";
			PreparedStatement st = con.prepareStatement(query);
			st.setString(1, meta.getPictureUri());
			st.setString(2, meta.getDescription());
			st.setString(3, meta.getEmail());
			st.setString(4, meta.getPhone());
			st.setInt(5, userId);
			return st.executeUpdate();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}
		return -1;
	}

}