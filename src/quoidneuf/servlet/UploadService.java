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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

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

import quoidneuf.dao.DaoProvider;
import quoidneuf.dao.DiscussionDao;
import quoidneuf.dao.MessageDao;
import quoidneuf.dao.SubscriberDao;
import quoidneuf.dao.SubscriberMetaDao;
import quoidneuf.util.Matcher;

/**
 * @author Edouard
 *
 */
@MultipartConfig(maxFileSize = 1024 * 1024, fileSizeThreshold = 1024 * 1024)
@WebServlet("/api/files")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"user", "super-user", "admin"}))
public class UploadService extends JsonServlet {

	private static final long serialVersionUID = -1041983016810957406L;
	
	public static final String SUBSCRIBER_PATH_KEY = "subscribers";
	public static final String DISCUSSION_PATH_KEY = "discussions";
	
	private SubscriberDao subscriberDao;
	private SubscriberMetaDao subscriberMetaDao;
	private DiscussionDao discussionDao;
	private MessageDao messageDao;
	
	public UploadService() {
		this.subscriberDao = DaoProvider.getDao(SubscriberDao.class);
		this.subscriberMetaDao = DaoProvider.getDao(SubscriberMetaDao.class);
		this.discussionDao = DaoProvider.getDao(DiscussionDao.class);
		this.messageDao = DaoProvider.getDao(MessageDao.class);
	}
	
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
							sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "paramètre 'dest' manquant");
						}
						else if (fileItem == null || fileItem.isFormField()) {
							sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "aucun fichier à uploader");
						}
						else if (folderItem == null || !folderItem.isFormField()) {
							sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "paramètre 'folder' manquant");
						}
						else {
							String destStr = destItem.getString();
							String folder = folderItem.getString();
							if (destStr.equals(SUBSCRIBER_PATH_KEY) || destStr.equals(DISCUSSION_PATH_KEY)) {
								Path contextPath = Paths.get(ctx.getRealPath("/"));
								Path root = contextPath.resolve(Paths.get(ctx.getInitParameter(destStr), folder));
								Path content = root.resolve(fileItem.getName());
								Path relativeContent;
								
								if (destStr.equals(SUBSCRIBER_PATH_KEY)) {
									if (Matcher.isDigits(folder) && subscriberDao.exist(Integer.parseInt(folder))) {
										if (Integer.parseInt(folder) == userId) {
											relativeContent = copy(fileItem, contextPath, root, content);
											subscriberMetaDao.updateSubscriberPicture(userId, relativeContent.toString());
											sendJson(HttpServletResponse.SC_CREATED, res, "{\"uri\" : \"" + relativeContent.getFileName() + "\"}");
										}
										else {
											sendTicket(HttpServletResponse.SC_FORBIDDEN, res, "accès utilisateur interdit");
										}
									}
									else {
										sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "utilisateur " + folder + " introuvable");
									}
								}
								else if (discussionDao.exist(folder)) {
									if (discussionDao.userExistIn(folder, userId)) {
										relativeContent = copy(fileItem, contextPath, root, content);
										String uuid = UUID.randomUUID().toString();
										messageDao.insertMessage(uuid, folder, userId, relativeContent.toString());
										sendJson(HttpServletResponse.SC_CREATED, res, "{\"uri\" : \"" + uuid + "\"}");										
									}
									else {
										sendTicket(HttpServletResponse.SC_FORBIDDEN, res, "discussion interdite");
									}
								}
								else {
									sendTicket(HttpServletResponse.SC_BAD_REQUEST, res, "discussion " + folder + " introuvable");
								}
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
	
	private Path copy(FileItem fileItem, Path contextPath, Path root, Path content) throws IOException {
		try {
			Files.createDirectories(root);
		} catch (IOException e) {}
		Files.copy(fileItem.getInputStream(), content, StandardCopyOption.REPLACE_EXISTING);
		return contextPath.relativize(content);
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
