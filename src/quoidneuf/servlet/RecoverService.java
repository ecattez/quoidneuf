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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quoidneuf.dao.CredentialDao;
import quoidneuf.dao.DaoProvider;
import quoidneuf.dao.SubscriberDao;
import quoidneuf.util.Matcher;

@WebServlet("/api/recover")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL))
public class RecoverService extends JsonServlet {

	private static final long serialVersionUID = -7213105238757666032L;
	
	private CredentialDao authenticationDao;
	private SubscriberDao subscriberDao;
	
	public RecoverService() {
		this.authenticationDao = DaoProvider.getDao(CredentialDao.class);
		this.subscriberDao = DaoProvider.getDao(SubscriberDao.class);
	}
	
	/** Demande de récupération de mot de passe */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String login = req.getParameter("username");
		String email = req.getParameter("email");
		
		if (login == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "login manquant");
		}
		else if (email == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "adresse email manquante");	
		}
		else if (!Matcher.isEmail(email)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "adresse email incorrecte");
		}
		else if (!subscriberDao.exist(login, email)) {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "login ou email incorrect");
		}
		else {
			String password = authenticationDao.resetPassword(login);
			String firstname = subscriberDao.getByLogin(login).getFirstName();
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/api/mails");
			StringBuilder builder = new StringBuilder();
			builder.append("<h1>Bonjour " + firstname + "</h1>");
			builder.append("<p>Tu as demandé à réinitialiser ton mot de passe, voici donc tes nouveaux identifiants:</p>");
			builder.append("<ul><li>Login: " + login + "</li>");
			builder.append("<li>Password: " + password + "</li></ul>");
			builder.append("<p>Nous te conseillons de changer de mot de passe dès que tu te seras connecté sur Quoidneuf</p>");
			builder.append("<hr>");
			builder.append("<b>A bientôt - Team Quoidneuf</b>");
			req.setAttribute("email", email);
			req.setAttribute("title", "[Quoidneuf] Réinitialisation de mot de passe");
			req.setAttribute("content", builder.toString());
			dispatcher.forward(req, res);
		}		
	}

}
