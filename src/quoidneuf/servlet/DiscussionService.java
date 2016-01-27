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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import quoidneuf.dao.DaoProvider;
import quoidneuf.dao.DiscussionDao;
import quoidneuf.dao.SubscriberDao;
import quoidneuf.entity.Discussion;
import quoidneuf.entity.Subscriber;
import quoidneuf.util.Matcher;

@WebServlet("/api/discussions")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"user", "super-user", "admin"}))
public class DiscussionService extends JsonServlet {
	
	private static final long serialVersionUID = 5334642516196786048L;
	
	private DiscussionDao discussionDao;
	private SubscriberDao subscriberDao;
	
	public DiscussionService() {
		this.discussionDao = DaoProvider.getDao(DiscussionDao.class);
		this.subscriberDao = DaoProvider.getDao(SubscriberDao.class);
	}

	/** Récupérer une discussion */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		String discussionId = req.getParameter("id");
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (discussionId == null) {
			sendJson(res, discussionDao.getDiscussionsOf(userId));
		}
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
		else {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "discussion non trouvée");
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
			String uuid = UUID.randomUUID().toString();
			int insert = discussionDao.insertDiscussion(uuid, discussionName);
			if (insert == -1) {
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la création de la discussion");
			}
			else if (discussionDao.insertUserIn(uuid, userId) > 0) {					
				sendJson(HttpServletResponse.SC_CREATED, res, "{\"id\" : \"" + uuid + "\"}");
			}
			else {
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de l'ajout de l'utilisateur dans la discussion");
			}
		}
	}
	
	/** Ajouter un ou plusieurs contacts à une discussion */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		String discussionId = req.getParameter("id");
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
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de l'ajout de l'utilisateur dans la discussion");
			}
		}
	}
	
	/** Quitter une discussion */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		String discussionId = req.getParameter("id");
		
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
			sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la suppression de l'utilisateur dans la discussion");
		}
	}

}
