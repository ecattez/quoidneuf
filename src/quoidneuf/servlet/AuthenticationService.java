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
package quoidneuf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import quoidneuf.dao.CredentialDao;
import quoidneuf.dao.DaoProvider;
import quoidneuf.dao.SubscriberDao;
import quoidneuf.util.Matcher;

@WebServlet("/api/authentication")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL))
public class AuthenticationService extends JsonServlet {
	
	private static final long serialVersionUID = 7229118350560492306L;
	
	private SubscriberDao subscriberDao;
	private CredentialDao authenticationDao;
	
	public AuthenticationService() {
		this.subscriberDao = DaoProvider.getDao(SubscriberDao.class);
		this.authenticationDao = DaoProvider.getDao(CredentialDao.class);
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
					int userId = subscriberDao.getIdByLogin(username);
					session.setAttribute("user", userId);
					sendTicket(HttpServletResponse.SC_CREATED, res, "connexion réussie");
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
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la modification du mot de passe");
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
