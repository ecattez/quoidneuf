package quoidneuf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import quoidneuf.dao.SubscriberDao;
import quoidneuf.entity.Subscriber;

@WebServlet("/api/authentication")
public class Authentication extends JsonServlet {
	
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
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			
			if (username == null) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "login manquant");
			}
			else if (password == null) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "mot de passe manquant");
			}
			else {
				try {
					HttpSession session = req.getSession();
					req.login(username, password);
					Subscriber subscriber = subscriberDao.getByLogin(username);
					session.setAttribute("user", subscriber.getId());
					sendJson(HttpServletResponse.SC_CREATED, res, subscriber);
				} catch (ServletException e) {
					sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "login ou mot de passe incorrect");
				}
			}
		}
		else {
			sendTicket(HttpServletResponse.SC_NOT_MODIFIED, res, "utilisateur déjà connecté");
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
			res.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			sendTicket(HttpServletResponse.SC_UNAUTHORIZED, res, "utilisateur non connecté");
		}
	}

}
