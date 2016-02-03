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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;

import quoidneuf.dao.CredentialDao;
import quoidneuf.dao.DaoProvider;
import quoidneuf.dao.SubscriberDao;
import quoidneuf.dao.SubscriberMetaDao;
import quoidneuf.entity.Credential;
import quoidneuf.entity.Subscriber;
import quoidneuf.entity.SubscriberMeta;
import quoidneuf.util.Matcher;

@WebServlet("/api/profiles")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL))
public class ProfileService extends JsonServlet {
	
	private static final long serialVersionUID = -7109249752388549689L;

	private CredentialDao credentialDao;
	private SubscriberDao subscriberDao;
	private SubscriberMetaDao subscriberMetaDao;
	
	public ProfileService() {
		this.credentialDao = DaoProvider.getDao(CredentialDao.class);
		this.subscriberDao = DaoProvider.getDao(SubscriberDao.class);
		this.subscriberMetaDao = DaoProvider.getDao(SubscriberMetaDao.class);
	}
	
	/** Récupérer un profil */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Integer profilId = Matcher.convertInt(req.getParameter("id"));
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (profilId == null) {
			profilId = userId;
		}
		if (!subscriberDao.exist(profilId)) {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "utilisateur " + profilId + " inexistant");
		}
		else {
			Subscriber subscriber = subscriberDao.getById(profilId);
			SubscriberMeta meta = subscriberMetaDao.fromSubscriber(profilId);
			subscriber.setMeta(meta);
			sendJson(res, subscriber);
		}
	}
	
	/** Créer un profil (+ authentification) */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String login = req.getParameter("username");
		if (credentialDao.exist(login)) {
			sendTicket(HttpServletResponse.SC_CONFLICT, res, "l'utilisateur " + login + " existe déjà");
		}
		else {
			String password = req.getParameter("password");
			String firstname = req.getParameter("firstname");
			String lastname = req.getParameter("lastname");
			String birthday = req.getParameter("birthday");
			String description = req.getParameter("description");
			String email = req.getParameter("email");
			String phone = req.getParameter("phone");
			if (Matcher.isEmpty(password)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "mot de passe inexistant");
			}
			else if (!Matcher.isPassword(password)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "longueur du mot de passe < à " + Credential.MIN_PASSWORD_LENGTH);
			}
			else if (Matcher.isEmpty(firstname)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "prénom manquant");
			}
			else if (Matcher.isEmpty(lastname)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "nom de famille manquant");
			}
			else if (Matcher.isEmpty(birthday)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "date d'anniversaire manquante");
			}
			else if (!Matcher.isDate(birthday)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "date d'anniversaire incorrecte");
			}
			else if (Matcher.isEmpty(email)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "adresse email manquante");
			}
			else if (!Matcher.isEmail(email)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "adresse email incorrecte");
			}
			else if (!Matcher.isEmpty(phone) && !Matcher.isPhone(phone)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "numéro de téléphone incorrect");
			}
			else if (credentialDao.insert(login, password) == -1) {
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la création du compte");
			}
			else if (subscriberDao.insert(login, firstname, lastname, birthday) == -1) {
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la création du profil");
			}
			else if (subscriberMetaDao.insert(null, description, email, phone) == -1) {
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la création du profil");
			}
			else {
				RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/api/mails");
				StringBuilder builder = new StringBuilder();
				builder.append("<h1>Bienvenue " + firstname + " !</h1>");
				builder.append("<p>Tu as créé ton compte Quoidneuf dont voici les identifiants: </p>");
				builder.append("<ul><li>Login: " + login + "</li>");
				builder.append("<li>Password: " + password + "</li></ul>");
				builder.append("<p>Tu peux dès à présent te connecter sur Quoidneuf !</p>");
				builder.append("<hr>");
				builder.append("<b>A bientôt - Team Quoidneuf</b>");
				req.setAttribute("email", email);
				req.setAttribute("title", "[Quoidneuf] Inscription réussie !");
				req.setAttribute("content", builder.toString());
				dispatcher.forward(req, res);
			}
		}
	}
	
	/** Modifier le profil */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		
		String password = req.getParameter("password");
		String description = req.getParameter("description");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (Matcher.isEmpty(email)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "adresse email non spécifiée");
		}
		else if (!Matcher.isEmail(email)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "adresse email incorrecte");
		}
		else if (!Matcher.isEmpty(phone) && !Matcher.isPhone(phone)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "numéro de téléphone incorrect");
		}
		else {
			boolean ok = true;
			if (!Matcher.isEmpty(password)) {
				if (!Matcher.isPassword(password)) {
					sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "longueur du mot de passe < à " + Credential.MIN_PASSWORD_LENGTH);
				}
				ok = credentialDao.changePassword(req.getRemoteUser(), password) > 0;
			}
			if (!ok) {
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors du changement du mot de passe");
			}
			else {
				if (!Matcher.isEmpty(description)) {
					description = StringEscapeUtils.escapeHtml4(description);
				}
				SubscriberMeta meta = new SubscriberMeta();
				meta.setDescription(description);
				meta.setEmail(email);
				meta.setPhone(phone);
				int update = subscriberMetaDao.updateFromSubscriber(userId, meta);
				if (update > 0) {
					res.sendError(HttpServletResponse.SC_NO_CONTENT);
				}
				else if (update == 0) {
					res.sendError(HttpServletResponse.SC_NOT_MODIFIED);
				}
				else {				
					sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la mise à jour du profil");
				}
			}
		}
	}
	
	/** Supprimer le profil (suppression logique, c'est à dire qu'on ne supprime que les données d'authentification) */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (credentialDao.remove(req.getRemoteUser()) > 0) {
			res.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la suppression du profil");
		}
	}

}
