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
import quoidneuf.dao.SubscriberDao;
import quoidneuf.dao.SubscriberMetaDao;
import quoidneuf.entity.Subscriber;
import quoidneuf.entity.SubscriberMeta;
import quoidneuf.util.Matcher;

@WebServlet("/api/profiles")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"user", "super-user", "admin"}))
public class ProfileService extends JsonServlet {
	
	private static final long serialVersionUID = -7109249752388549689L;

	private SubscriberDao subscriberDao;
	private SubscriberMetaDao subscriberMetaDao;
	
	public ProfileService() {
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
			sendTicket(HttpServletResponse.SC_BAD_GATEWAY, res, "paramètre 'id' manquant");
		}
		else if (!subscriberDao.exist(profilId)) {
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
		
	}
	
	/** Modifier le profil */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
	
	/** Supprimer le profil (suppression logique) */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		Integer userId = (Integer) session.getAttribute("user");
		
		if (userId == null) {
			res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

}
