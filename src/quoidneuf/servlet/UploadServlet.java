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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import static quoidneuf.util.AppServletListener.*;
import quoidneuf.util.Matcher;

/**
 * @author Edouard
 *
 */
@MultipartConfig(maxFileSize = 1024 * 1024)
@WebServlet("/api/files")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"user", "super-user", "admin"}))
public class UploadServlet extends JsonServlet {

	private static final long serialVersionUID = -1041983016810957406L;
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (!req.getContentType().contains("multipart/form-data")) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "content-type incorrect");
		}
		else {
			ServletContext ctx = req.getServletContext();
			HttpSession session = req.getSession(true);
			Integer userId = (Integer) session.getAttribute("user");
			String dest = req.getParameter("dest");
			
			if (userId == null) {
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
			else if (Matcher.isEmpty(dest)) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "parametre 'dest' manquant");
			}
			else if (!(dest.equals(SUBSCRIBER_PATH_KEY) || dest.equals(DISCUSSION_PATH_KEY)) || ctx.getAttribute(dest) == null) {
				sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "destination non trouvée");
			}
			else {
				try {
					Part part = req.getPart("file");
					String filename = getFileName(part);
					Path target = Paths.get(dest, String.valueOf(userId));
					try {
						Files.createDirectories(target);
						Files.copy(part.getInputStream(), target.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
						sendTicket(HttpServletResponse.SC_CREATED, res, "fichier uploadé");
					} catch (IOException e) {
						e.printStackTrace();
						sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la copie du fichier");
					}
				} catch (Exception e) {
					sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "aucun fichier à télécharger");
				}
			}
		}
	}
	
	/** Récupère le nom du fichier dans le content-disposition de l'en-tête http */	
	private String getFileName(Part part) {
		for (String cd : part.getHeader("content-disposition").split(";")) {
			if (cd.trim().startsWith("filename")) {
				return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}

}
