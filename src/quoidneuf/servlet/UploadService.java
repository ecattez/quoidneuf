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

import static quoidneuf.util.AppServletListener.DISCUSSION_PATH_KEY;
import static quoidneuf.util.AppServletListener.SUBSCRIBER_PATH_KEY;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author Edouard
 *
 */
@MultipartConfig(maxFileSize = 1024 * 1024, fileSizeThreshold = 1024 * 1024)
@WebServlet("/api/files")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"user", "super-user", "admin"}))
public class UploadService extends JsonServlet {

	private static final long serialVersionUID = -1041983016810957406L;
	
	/** Upload un fichier sur le serveur */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (!ServletFileUpload.isMultipartContent(req)) {
			sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "content-type incorrect");
		}
		else {
			ServletContext ctx = req.getServletContext();
			HttpSession session = req.getSession(true);
			Integer userId = (Integer) session.getAttribute("user");
			
			if (userId == null) {
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
			else {
				// Create a factory for disk-based file items
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// Configure a repository (to ensure a secure temp location is used)
				File repository = (File) ctx.getAttribute("javax.servlet.context.tempdir");
				factory.setRepository(repository);
				ServletFileUpload upload = new ServletFileUpload(factory);
				try {
					List<FileItem> formItems = upload.parseRequest(req);
					if (formItems == null || formItems.size() == 0) {
						sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "aucun fichier à uploader");
					}
					else {
						FileItem destItem = getFileItem("dest", formItems);
						FileItem folderItem = getFileItem("folder", formItems);
						FileItem fileItem = getFileItem("file", formItems);
						
						if (destItem == null || !destItem.isFormField()) {
							sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "parametre 'dest' manquant");
						}
						else if (fileItem == null || fileItem.isFormField()) {
							sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "aucun fichier à uploader");
						}
						else {
							String destStr = destItem.getString();
							System.out.println(ctx.getAttribute(destStr));
							if (destStr.equals(SUBSCRIBER_PATH_KEY) || destStr.equals(DISCUSSION_PATH_KEY)) {
								Path dest = ((Path) ctx.getAttribute(destStr)).resolve(folderItem.getString());
								try {
									Files.createDirectories(dest);
								} catch (IOException e) {}
								Files.copy(fileItem.getInputStream(), dest.resolve(fileItem.getName()));
								sendJson(res, "fichier uploadé");
							}
							else {
								sendTicket(HttpServletResponse.SC_NOT_FOUND, res, "destination non trouvée");
							}
						}
					}
				} catch (FileUploadException e) {
					e.printStackTrace();
					sendTicket(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, res, "erreur lors de la copie du fichier");
				}
			}
		}
	}
	
	private FileItem getFileItem(String field, List<FileItem> formItems) {
		for (FileItem item : formItems) {
			if (field.equals(item.getFieldName())) {
				return item;
			}
		}
		return null;
	}

}
