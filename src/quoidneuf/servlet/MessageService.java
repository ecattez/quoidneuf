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

import quoidneuf.dao.DiscussionDao;
import quoidneuf.dao.MessageDao;
import quoidneuf.util.Matcher;

@WebServlet("/api/messages")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"user", "super-user", "admin"}))
public class MessageService extends JsonServlet {

	private static final long serialVersionUID = 1733305686404777515L;

	private DiscussionDao discussionDao;
	private MessageDao messageDao;
	
	public MessageService() {
		discussionDao = new DiscussionDao();
		messageDao = new MessageDao();
	}
	
	/** Ecrire un message dans une discussion */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		Integer discussionId = Matcher.convert(req.getParameter("discussion"));
		String content = req.getParameter("content");
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
		else if (discussionId == null) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		else if (!discussionDao.exist(discussionId)) {
			res.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		else if (content == null) {
			res.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		else {
			int id = messageDao.insertMessage(discussionId, userId, content);
			if (id > 0) {
				sendJson(res, id);
			}
			else {
				res.sendError(HttpServletResponse.SC_EXPECTATION_FAILED);
			}
		}
	}

}
