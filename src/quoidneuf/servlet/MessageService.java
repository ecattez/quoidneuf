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
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;

import quoidneuf.dao.DaoProvider;
import quoidneuf.dao.DiscussionDao;
import quoidneuf.dao.MessageDao;

@WebServlet("/api/messages")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"user", "super-user", "admin"}))
public class MessageService extends JsonServlet {

	private static final long serialVersionUID = 1733305686404777515L;

	private DiscussionDao discussionDao;
	private MessageDao messageDao;
	
	public MessageService() {
		this.discussionDao = DaoProvider.getDao(DiscussionDao.class);
		this.messageDao = DaoProvider.getDao(MessageDao.class);
	}
	
	/** Ecrire un message dans une discussion */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		String discussionId = req.getParameter("discussion");
		String content = req.getParameter("content");
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (discussionId == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "discussion manquante");
		}
		else if (content == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "contenu manquant");
		}
		else if (!discussionDao.exist(discussionId)) {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "discussion introuvable");
		}
		else if (!discussionDao.userExistIn(discussionId, userId)) {
			sendTicket(HttpServletResponse.SC_FORBIDDEN, res, "utilisateur interdit dans la discussion");
		}
		else {
			String uuid = UUID.randomUUID().toString();
			int insert = messageDao.insertMessage(uuid, discussionId, userId, StringEscapeUtils.escapeHtml4(content));
			if (insert > 0) {
				sendJson(HttpServletResponse.SC_CREATED, res, "{\"id\" : \"" + uuid + "\"}");
			}
			else {
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de l'enregistrement du message");
			}
		}
	}

}
