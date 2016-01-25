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

import quoidneuf.dao.FriendDao;
import quoidneuf.dao.SubscriberDao;
import quoidneuf.util.Matcher;

@WebServlet("/api/friends")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.NONE, rolesAllowed = {"user", "super-user", "admin"}))
public class FriendService extends JsonServlet {
	
	private static final long serialVersionUID = -7109249752388549689L;
	
	private FriendDao friendDao;
	private SubscriberDao subscriberDao;
	
	public FriendService() {
		this.friendDao = new FriendDao();
		this.subscriberDao = new SubscriberDao();
	}
	
	/** Récupérer la liste des amis ou les demandes d'ajouts */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Boolean status = Matcher.convertBoolean(req.getParameter("status"));
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (status == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "status manquant");
		}
		else {
			sendJson(res, friendDao.getLinkedWith(userId, status));
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
			res.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
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
				res.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
			}
		}
		else {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "lien d'amitié introuvable");
		}
	}

}
