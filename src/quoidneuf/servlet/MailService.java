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

import quoidneuf.util.Matcher;

@WebServlet("/api/mails")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL))
public class MailService extends JsonServlet {
	
	private static final long serialVersionUID = -4717221731384614106L;

	/** Envoie d'un mail **/
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String email = (String) req.getAttribute("email");
		String title = (String) req.getAttribute("title");
		String content = (String) req.getAttribute("content");
		
		if (Matcher.isEmpty(email)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "adresse email manquante");
		}
		else if (!Matcher.isEmail(email)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "adresse email incorrecte");
		}
		else if (Matcher.isEmpty(title)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "sujet manquant");
		}
		else if (Matcher.isEmpty(content)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "contenu manquant");
		}
		else {
			try {
				Context initCtx = new InitialContext();
				Context envCtx = (Context) initCtx.lookup("java:comp/env");
				javax.mail.Session sess = (javax.mail.Session) envCtx.lookup("mail/Session");
				Message message = new MimeMessage(sess);
				message.setFrom(new InternetAddress("edouard.cattez@sfr.fr"));
				InternetAddress to[] = new InternetAddress[1];
				to[0] = new InternetAddress(email);
				message.setRecipients(Message.RecipientType.TO, to);
				message.setSubject(title);
				message.setContent(content, "text/html");
				Transport.send(message);
				sendTicket(HttpServletResponse.SC_CREATED, res, "email envoyé à l'adresse " + email);
			} catch (NamingException | MessagingException e) {
				e.printStackTrace();
				sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de l'envoie du mail à l'adresse " + email);
			}
		}
	}

}
