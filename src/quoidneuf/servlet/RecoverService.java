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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/recover")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL))
public class RecoverService extends HttpServlet {

	private static final long serialVersionUID = -7213105238757666032L;
	
	/** Demande de récupération de mot de passe */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Context initCtx;
		try {
			initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			javax.mail.Session sess = (javax.mail.Session) envCtx.lookup("mail/Session");
			Message message = new MimeMessage(sess);
			message.setFrom(new InternetAddress("edouard.cattez@sfr.fr"));
			InternetAddress to[] = new InternetAddress[1];
			to[0] = new InternetAddress("t.ferro184@gmail.com");
			message.setRecipients(Message.RecipientType.TO, to);
			message.setSubject("[Quoidneuf] Réinitialisation de mot de passe");
			message.setContent("Bonjour Thomas", "text/plain");
			Transport.send(message);
			res.setStatus(HttpServletResponse.SC_CREATED);
		} catch (NamingException | MessagingException e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
