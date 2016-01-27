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
