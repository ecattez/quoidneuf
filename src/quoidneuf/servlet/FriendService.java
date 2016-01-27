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

import quoidneuf.dao.DaoProvider;
import quoidneuf.dao.FriendDao;
import quoidneuf.dao.SubscriberDao;
import quoidneuf.util.Matcher;

@WebServlet("/api/friends")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"user", "super-user", "admin"}))
public class FriendService extends JsonServlet {
	
	private static final long serialVersionUID = -7109249752388549689L;
	
	private FriendDao friendDao;
	private SubscriberDao subscriberDao;
	
	public FriendService() {
		this.friendDao = DaoProvider.getDao(FriendDao.class);
		this.subscriberDao = DaoProvider.getDao(SubscriberDao.class);
	}
	
	/** Récupérer la liste des amis ou les demandes d'ajouts */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Integer profilId = Matcher.convertInt(req.getParameter("id"));
		Boolean status = Matcher.convertBoolean(req.getParameter("status"));
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (status == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "status manquant");
		}
		else {
			if (profilId == null) {
				profilId = userId;
			}
			sendJson(res, friendDao.getLinkedWith(profilId, status));
		}
	}
	
	/** Faire une demande d'ami */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Integer friendId = Matcher.convertInt(req.getParameter("friend"));
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (friendId == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "paramètre 'friend' manquant");
		}
		else if (userId.intValue() == friendId.intValue()) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "l'utilisateur ne peut pas s'ajouter lui-même");
		}
		else if (!subscriberDao.exist(friendId)) {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "utilisateur " + friendId + " inexistant");
		}
		else if (friendDao.areLinked(userId, friendId)) {
			sendTicket(HttpServletResponse.SC_CONFLICT, res, "amitié déjà existante");
		}
		else if (friendDao.requestForFriendship(userId, friendId) > 0) {
			sendTicket(HttpServletResponse.SC_CREATED, res, "demande d'ajout créée");
		}
		else {
			sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la demande d'ajout");
		}
	}
	
	/** Confirmer une demande d'ami */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Integer friendId = Matcher.convertInt(req.getParameter("friend"));
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (friendId == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "paramètre 'friend' manquant");
		}
		else if (friendDao.areFriends(userId, friendId)) {
			sendTicket(HttpServletResponse.SC_NOT_MODIFIED, res, "amitié déjà existante");
		}
		else if (friendDao.answerToRequest(userId, friendId) > 0) {
			res.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "lien d'amitié introuvable");
		}
	}
	
	/** Supprimer d'un lien d'amitié (confirmé ou non) */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Integer friendId = Matcher.convertInt(req.getParameter("friend"));
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (friendId == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "paramètre 'friend' manquant");
		}
		else if (friendDao.areLinked(userId, friendId)) {
			if (friendDao.removeFriendship(userId, friendId) > 0) {
				res.sendError(HttpServletResponse.SC_NO_CONTENT);
			}
			else {
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la suppression du lien d'amitié");
			}
		}
		else {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "lien d'amitié introuvable");
		}
	}

}
