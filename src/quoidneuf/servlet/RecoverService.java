package quoidneuf.servlet;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import quoidneuf.dao.AuthenticationDao;
import quoidneuf.dao.DaoProvider;
import quoidneuf.dao.SubscriberDao;

@WebServlet("/api/recover")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL))
public class RecoverService extends JsonServlet {

	private static final long serialVersionUID = -7213105238757666032L;
	
	private AuthenticationDao authenticationDao;
	private SubscriberDao subscriberDao;
	
	public RecoverService() {
		this.authenticationDao = DaoProvider.getDao(AuthenticationDao.class);
		this.subscriberDao = DaoProvider.getDao(SubscriberDao.class);
	}
	
	/** Demande de récupération de mot de passe */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String login = req.getParameter("login");
		String email = req.getParameter("email");
		
		if (login == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "login manquant");
		}
		else if (email == null) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "email manquant");	
		}
		else if (!subscriberDao.exist(login, email)) {
			sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "login ou email incorrect");
		}
		else {
			String password = authenticationDao.resetPassword(login);
			String firstname = subscriberDao.getByLogin(login).getFirstName();
			if (sendMessage(firstname, login, password, email)) {
				sendTicket(HttpServletResponse.SC_CREATED, res, "email envoyé à l'adresse " + email);
			}
			else {
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de l'envoie du mail à l'adresse " + email);
			}
		}		
	}
	
	private boolean sendMessage(String firstname, String login, String password, String email) {
		StringBuilder builder = new StringBuilder();
		builder.append("<h1>Bonjour " + firstname + "</h1>");
		builder.append("<p>Tu as demandé à réinitialiser ton mot de passe, voici donc tes nouveaux identifiants:</p>");
		builder.append("<ul><li>Login: " + login + "</li>");
		builder.append("<li>Password: " + password + "</li></ul>");
		builder.append("<p>Nous te conseillons de changer de mot de passe dès que tu te seras connecté sur Quoidneuf</p>");
		builder.append("<hr>");
		builder.append("<b>Merci de ta fidélité - Team Quoidneuf</b>");
		
		try {
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			javax.mail.Session sess = (javax.mail.Session) envCtx.lookup("mail/Session");
			Message message = new MimeMessage(sess);
			message.setFrom(new InternetAddress("edouard.cattez@sfr.fr"));
			InternetAddress to[] = new InternetAddress[1];
			to[0] = new InternetAddress(email);
			message.setRecipients(Message.RecipientType.TO, to);
			message.setSubject("[Quoidneuf] Réinitialisation de mot de passe");
			message.setContent(builder.toString(), "text/html");
			Transport.send(message);
			return true;
		} catch (NamingException | MessagingException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
