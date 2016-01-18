package quoidneuf.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quoidneuf.entity.Discussion;
import quoidneuf.entity.Message;
import quoidneuf.entity.Subscriber;
import quoidneuf.util.Matcher;
import quoidneuf.util.Pool;

@WebServlet("/api/discussion")
public class DiscussionService extends JsonServlet {
	
	private static final long serialVersionUID = 5334642516196786048L;

	/** Récupérer une discussion */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strDiscussionId = req.getParameter("id");
		String strUserId = req.getParameter("user");
		if (Matcher.isDigits(strDiscussionId) && Matcher.isDigits(strUserId)) {
			try (Connection con = Pool.getConnection()) {				
				int id = Integer.parseInt(strDiscussionId);
				int userId = Integer.parseInt(strUserId);
				if (userExist(con, id, userId)) {
					// On récupère tous les messages (avec l'utilisateur qui l'a écrit)
					Discussion d = loadFromId(con, id);
					if (d == null) {
						res.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
					else {
						// On récupère tous les utilisateurs présents dans la discussion
						// Ils peuvent ne pas avoir (encore) écrit de messages
						loadSubscribersInto(con, d);
						sendJson(res, d);
					}
				}
				else {
					res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			}
		}
		else if (Matcher.isDigits(strUserId)) {
			try (Connection con = Pool.getConnection()) {				
				List<Discussion> discussions = loadFromSubscriber(con, Integer.parseInt(strUserId));
				if (discussions.size() == 0) {
					res.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
				else {
					sendJson(res, discussions);
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/** Créer une discussion */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String discussionName = req.getParameter("name");
		if (discussionName == null || discussionName.length() == 0) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		else {
			try (Connection con = Pool.getConnection()) {
				// Création de la discussion
				String query = "INSERT INTO discussion (discussion_name, enabled) VALUES (?, true)";
				PreparedStatement st = con.prepareStatement(query);
				st.setString(1, discussionName);
				st.executeUpdate();
				
				// On récupère l'id de la discussion insérée
				query = "SELECT MAX(discussion_id) AS _to FROM discussion";
				st = con.prepareStatement(query);
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					sendJson(res, new Discussion(rs.getInt("_to"), discussionName));
				}
				else {
					res.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Ajouter un ou plusieurs contacts à une discussion */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strDiscussionId = req.getParameter("id");
		String strUserId = req.getParameter("user");
		String[] strUserIds = req.getParameterValues("users");

		if (Matcher.isDigits(strDiscussionId) && strUserIds != null) {
			try (Connection con = Pool.getConnection()) {
				// Création de la discussion
				String query = "INSERT INTO belong_to VALUES (?,?)";
				PreparedStatement st;
				
				for (String user : strUserIds) {
					if (Matcher.isDigits(strUserId)) {
						st = con.prepareStatement(query);
						st.setInt(1, Integer.parseInt(user));
						st.setInt(2, Integer.parseInt(strDiscussionId));
						try {
							st.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
				res.sendError(HttpServletResponse.SC_CREATED);
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/** Quitter une discussion */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strDiscussionId = req.getParameter("id");
		String strUserId = req.getParameter("user");
		
		if (Matcher.isDigits(strDiscussionId) && Matcher.isDigits(strUserId)) {
			try (Connection con = Pool.getConnection()) {
				int id = Integer.parseInt(strDiscussionId);
				int userId = Integer.parseInt(strUserId);
				if (userExist(con, id, userId)) {
					String query = "DELETE FROM belong_to WHERE subscriber_id = ? AND discussion_id = ?";
					PreparedStatement st = con.prepareStatement(query);
					st.setInt(1, Integer.parseInt(strUserId));
					st.setInt(2, Integer.parseInt(strDiscussionId));
					
					if (st.executeUpdate() == 1) {
						res.sendError(HttpServletResponse.SC_NO_CONTENT);					
					}
					else {
						res.sendError(HttpServletResponse.SC_NOT_FOUND);
					}
				}
				else {
					res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			}
		}
		else {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param id
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	private boolean userExist(Connection con, int id, int userId) throws SQLException {
		String query = "SELECT 1 FROM belong_to WHERE discussion_id = ? AND subscriber_id = ?";
		PreparedStatement st = con.prepareStatement(query);
		st.setInt(1, id);
		st.setInt(2, userId);
		ResultSet rs = st.executeQuery();
		return rs.next();
	}
	
	/**
	 * 
	 * @param con
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	private Discussion loadFromId(Connection con, int id) throws SQLException {
		String query = "SELECT * FROM subscriber_message_discussion WHERE _to = ? LIMIT 100";
		PreparedStatement st = con.prepareStatement(query);
		st.setInt(1, id);
		ResultSet rs = st.executeQuery();
		Discussion d = null;
		while (rs.next()) {
			if (d == null) {
				d = new Discussion();
				d.setId(rs.getInt("_to"));
				d.setName(rs.getString("_name"));
			}
			if (rs.getTimestamp("_date") != null) {
				Message m = new Message();
				Subscriber s = new Subscriber(rs.getInt("_from"), rs.getString("_first_name"), rs.getString("_last_name"));
				m.setSubscriber(s);
				m.setContent(rs.getString("_content"));
				m.setWrittenDate(rs.getTimestamp("_date"));
				d.push(m);
			}
		}
		return d;
	}
	
	/**
	 * 
	 * @param con
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	private List<Discussion> loadFromSubscriber(Connection con, int userId) throws SQLException {
		String query = "SELECT _to, _name FROM discussion_trie WHERE _to IN (SELECT discussion_id FROM belong_to WHERE subscriber_id = ?)";
		PreparedStatement st = con.prepareStatement(query);
		st.setInt(1, userId);
		ResultSet rs = st.executeQuery();
		List<Discussion> discussions = new ArrayList<>();
		while (rs.next()) {
			discussions.add(new Discussion(rs.getInt("_to"), rs.getString("_name")));
		}
		return discussions;
	}
	
	/**
	 * 
	 * @param con
	 * @param d
	 * @return
	 * @throws SQLException
	 */
	private Discussion loadSubscribersInto(Connection con, Discussion d) throws SQLException {
		String query = "SELECT subscriber_id AS _from, first_name AS _first_name, last_name AS _last_name"
				+ " FROM subscriber WHERE subscriber_id IN (SELECT subscriber_id FROM belong_to WHERE discussion_id = ?)";
		PreparedStatement st = con.prepareStatement(query);
		st.setInt(1, d.getId());
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			Subscriber s = new Subscriber();
			s.setId(rs.getInt("_from"));
			s.setFirstName(rs.getString("_first_name"));
			s.setLastName(rs.getString("_last_name"));
			d.push(s);
		}
		return d;
	}

}
