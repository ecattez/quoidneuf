package quoidneuf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import quoidneuf.dao.AuthenticationDao;
import quoidneuf.dao.DaoProvider;
import quoidneuf.dao.SubscriberDao;
import quoidneuf.entity.Subscriber;
import quoidneuf.util.Matcher;

@WebServlet("/api/authentication")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL))
public class AuthenticationService extends JsonServlet {
	
	/* https://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html */
	
	private static final long serialVersionUID = 7229118350560492306L;
	
	private SubscriberDao subscriberDao;
	private AuthenticationDao authenticationDao;
	
	public AuthenticationService() {
		this.subscriberDao = DaoProvider.getDao(SubscriberDao.class);
		this.authenticationDao = DaoProvider.getDao(AuthenticationDao.class);
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
	 * Modifie le mot de passe de l'utilisateur
	 */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (req.getUserPrincipal() == null) {
			sendTicket(HttpServletResponse.SC_UNAUTHORIZED, res, "utilisateur non connecté");
		}
		else {
			String password = req.getParameter("password");
			if (Matcher.isEmpty(password)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "paramètre 'password' manquant");
			}
			else if (authenticationDao.changePassword(req.getRemoteUser(), password) > 0) {
				res.sendError(HttpServletResponse.SC_NO_CONTENT);
			}
			else {
				res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		}
	}
	
	/**
	 * Permet à un client de se déconnecter
	 */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (req.getUserPrincipal() == null) {
			sendTicket(HttpServletResponse.SC_UNAUTHORIZED, res, "utilisateur non connecté");
		}
		else {
			HttpSession session = req.getSession();
			session.invalidate();
			req.logout();
			res.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
	}

}
