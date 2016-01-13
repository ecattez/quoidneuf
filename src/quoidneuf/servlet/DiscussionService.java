package quoidneuf.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
		String idDiscussion = req.getParameter("id");
		if (Matcher.isDigits(idDiscussion)) {
			Connection con = null;
			try {
				con = Pool.getConnection();
				
				int id = Integer.parseInt(idDiscussion);
				// On récupère tous les messages (avec l'utilisateur qui l'a écrit)
				Discussion d = buildFromResultSet(con, id);
				
				// On récupère tous les utilisateurs présents dans la discussion
				// Ils peuvent ne pas avoir (encore) écrit de messages
				loadSubscribersInto(con, d);
				
				if (d == null) {
					res.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
				else {
					sendJson(res, d);
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/** Créer une discussion */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String name = req.getParameter("name");
		if (name == null || name.length() == 0) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		else {
			Connection con = null;
			try {
				con = Pool.getConnection();
				// Création de la discussion
				String query = "INSERT INTO discussion (discussion_name, enabled) VALUES (?, true)";
				PreparedStatement st = con.prepareStatement(query);
				st.setString(1, name);
				st.executeUpdate();
				
				// On récupère l'id de la discussion insérée
				query = "SELECT MAX(discussion_id) as id FROM discussion";
				st = con.prepareStatement(query);
				
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					sendJson(res, new Discussion(rs.getInt("id"), name));
				}
				else {
					res.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/** Ajouter un ou plusieurs contacts à une discussion */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String idDiscussion = req.getParameter("id");
		String[] idUsers = req.getParameterValues("user");

		if (Matcher.isDigits(idDiscussion) && idUsers != null) {
			Connection con = null;
			try {
				con = Pool.getConnection();
				// Création de la discussion
				String query = "INSERT INTO belong_to VALUES (?,?)";
				PreparedStatement st;
				
				for (String idUser : idUsers) {
					if (Matcher.isDigits(idUser)) {
						st = con.prepareStatement(query);
						st.setInt(1, Integer.parseInt(idUser));
						st.setInt(2, Integer.parseInt(idDiscussion));
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
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	/** Quitter une discussion */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String idDiscussion = req.getParameter("id");
		String idUser = req.getParameter("user");
		
		if (Matcher.isDigits(idDiscussion) && Matcher.isDigits(idUser)) {
			Connection con = null;
			try {
				con = Pool.getConnection();
				// Création de la discussion
				String query = "DELETE FROM belong_to WHERE subscriber_id = ? AND discussion_id = ?";
				PreparedStatement st = con.prepareStatement(query);
				st.setInt(1, Integer.parseInt(idUser));
				st.setInt(2, Integer.parseInt(idDiscussion));
				
				if (st.executeUpdate() == 1) {
					res.sendError(HttpServletResponse.SC_NO_CONTENT);					
				}
				else {
					res.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			} catch (NamingException | SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		else {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
	}
	
	private Discussion buildFromResultSet(Connection con, int id) throws SQLException {
		String query =
				"SELECT s.subscriber_id as _from, s.first_name as _first_name, s.last_name as _last_name, d.discussion_id as _to, d.discussion_name as _name, content as _content, written_date as _date"
				+ " FROM discussion d LEFT JOIN message m ON (d.discussion_id = m.discussion_id)"
				+ " LEFT JOIN subscriber s ON (m.subscriber_id = s.subscriber_id)"
				+ " WHERE d.discussion_id = ? ORDER BY written_date DESC LIMIT 100";
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
	
	private Discussion loadSubscribersInto(Connection con, Discussion d) throws SQLException {
		String query = "SELECT * FROM subscriber WHERE subscriber_id IN (SELECT subscriber_id FROM belong_to WHERE discussion_id = ?)";
		PreparedStatement st = con.prepareStatement(query);
		st.setInt(1, d.getId());
		ResultSet rs = st.executeQuery();
		while (rs.next()) {
			Subscriber s = new Subscriber();
			s.setId(rs.getInt("subscriber_id"));
			s.setFirstName(rs.getString("first_name"));
			s.setLastName(rs.getString("last_name"));
			d.push(s);
		}
		return d;
	}

}
