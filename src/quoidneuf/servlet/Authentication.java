package quoidneuf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import quoidneuf.dao.SubscriberDao;

@WebServlet("/api/authentication")
public class Authentication extends HttpServlet {
	
	private static final long serialVersionUID = 7229118350560492306L;
	
	private SubscriberDao subscriberDao;
	
	public Authentication() {
		subscriberDao = new SubscriberDao();
	}

	/**
	 * Permet à un client de se connecter avec un login et un password
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (req.getUserPrincipal() == null) {
			HttpSession session = req.getSession();
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			
			try {
				req.login(username, password);
				int id = subscriberDao.getIdByLogin(username);
				session.setAttribute("user", id);
			} catch (ServletException e) {
				res.sendError(HttpServletResponse.SC_BAD_REQUEST);
			}
		}
		else {
			res.sendError(HttpServletResponse.SC_NOT_MODIFIED);
		}
	}
	
	/**
	 * Permet à un client de se déconnecter
	 */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (req.getUserPrincipal() != null) {
			HttpSession session = req.getSession();
			session.invalidate();
			req.logout();
		}
		else {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

}
