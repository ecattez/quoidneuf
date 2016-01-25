package quoidneuf.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import quoidneuf.dao.DiscussionDao;
import quoidneuf.dao.SubscriberDao;
import quoidneuf.entity.Discussion;
import quoidneuf.entity.Subscriber;
import quoidneuf.util.Matcher;

@WebServlet("/api/discussions")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.NONE, rolesAllowed = {"user", "super-user", "admin"}))
public class DiscussionService extends JsonServlet {
	
	private static final long serialVersionUID = 5334642516196786048L;
	
	private DiscussionDao discussionDao;
	private SubscriberDao subscriberDao;
	
	public DiscussionService() {
		this.discussionDao = new DiscussionDao();
		this.subscriberDao = new SubscriberDao();
	}

	/** Récupérer une discussion */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Integer discussionId = Matcher.convertInt(req.getParameter("id"));
		
		// L'utilisateur n'est pas connecté, il n'est pas autorisé à aller plus loin
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		// Aucune discussion précisée, on récupère toutes les discussions de l'utilisateur
		else if (discussionId == null) {
			sendJson(res, discussionDao.getDiscussionsOf(userId));
		}
		// Au contraire, on charge la discussion et ses abonnés
		else if (discussionDao.exist(discussionId)) {
			if (!discussionDao.userExistIn(discussionId, userId)) {
				sendTicket(HttpServletResponse.SC_FORBIDDEN, res, "discussion interdite");
			}
			else {
				Discussion discussion = discussionDao.getDiscussion(discussionId);
				List<Subscriber> subscribers = discussionDao.getSubscribersOf(discussionId);
				discussion.setSubscribers(subscribers);
				sendJson(res, discussion);
			}
		}
		// Si la discussion n'existe pas, on renvoie 404
		else {
			sendTicket(HttpServletResponse.SC_FORBIDDEN, res, "discussion interdite");
		}
	}

	/** Créer une discussion */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		String discussionName = req.getParameter("name");
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (Matcher.isEmpty(discussionName)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "paramètre 'name' manquant");
		}
		else {
			int discussionId = discussionDao.insertDiscussion(discussionName);
			if (discussionId == -1) {
				res.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
			}
			else if (discussionDao.insertUserIn(discussionId, userId) > 0) {					
				sendJson(HttpServletResponse.SC_CREATED, res, discussionId);
			}
			else {
				res.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
			}
		}
	}
	
	/** Ajouter un ou plusieurs contacts à une discussion */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Integer discussionId = Matcher.convertInt(req.getParameter("id"));
		String[] strUserIds = req.getParameterValues("users");
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (discussionId == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "paramètre 'id' manquant");
		}
		else if (!discussionDao.exist(discussionId)) {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "discussion introuvable");
		}
		else if (!discussionDao.userExistIn(discussionId, userId)) {
			sendTicket(HttpServletResponse.SC_FORBIDDEN, res, "discussion interdite");
		}
		else {
			List<Integer> userIds = new ArrayList<>();
			for (String user : strUserIds) {
				if (Matcher.isDigits(user)) {
					userIds.add(Integer.parseInt(user));
				}
			}
			userIds = subscriberDao.correctIds(userIds);
			if (discussionDao.insertUsersIn(discussionId, userIds) > 0) {
				res.sendError(HttpServletResponse.SC_NO_CONTENT);
			}
			else {
				res.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
			}
		}
	}
	
	/** Quitter une discussion */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Integer discussionId = Matcher.convertInt(req.getParameter("id"));
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (discussionId == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "paramètre 'id' manquant");
		}
		else if (!discussionDao.exist(discussionId)) {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "discussion introuvable");
		}
		else if (!discussionDao.userExistIn(discussionId, userId)) {
			sendTicket(HttpServletResponse.SC_FORBIDDEN, res, "discussion interdite");
		}
		else if (discussionDao.removeUserFrom(discussionId, userId) > 0) {
			res.sendError(HttpServletResponse.SC_NO_CONTENT);
		}
		else {
			res.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
		}
	}

}
