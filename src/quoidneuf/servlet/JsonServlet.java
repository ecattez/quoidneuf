package quoidneuf.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import quoidneuf.entity.Ticket;

public abstract class JsonServlet extends HttpServlet {

	private static final long serialVersionUID = 3796747675330297774L;
	
	/**
	 * Envoie un objet au client au format JSON.
	 * 
	 * @param	httpResponse
	 * 			le code de la réponse HTTP
	 * @param	res
	 * 			la réponse HTTP
	 * @param	json
	 * 			l'objet JSONable
	 * @throws	IOException
	 * 			erreur à l'écriture de l'objet dans le flux de sortie
	 */
	public void sendJson(int httpResponse, HttpServletResponse res, Object json) throws IOException {
		PrintWriter out = (PrintWriter) res.getWriter();
		res.setStatus(httpResponse);
		res.setContentType("application/json");
		out.println(json);
	}
	
	/**
	 * Envoie un objet au client au format JSON au travers de la réponse SC_OK
	 * 
	 * @param	res
	 * 			la réponse HTTP
	 * @param	json
	 * 			l'objet JSONable
	 * @throws	IOException
	 * 			erreur à l'écriture de l'objet dans le flux de sortie
	 */
	public void sendJson(HttpServletResponse res, Object json) throws IOException {
		sendJson(HttpServletResponse.SC_OK, res, json);
	}
	
	/**
	 * Envoie un ticket au client
	 * 
	 * @param	httpResponse
	 * 			le code de la réponse HTTP
	 * @param	res
	 * 			la réponse HTTP
	 * @param	message
	 * 			le message du ticket
	 * @throws	IOException
	 * 			erreur à l'écriture de l'objet dans le flux de sortie
	 */
	public void sendTicket(int httpResponse, HttpServletResponse res, String message) throws IOException {
		sendJson(httpResponse, res, new Ticket(httpResponse, message));
	}
}
