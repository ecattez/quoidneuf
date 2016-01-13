package quoidneuf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/profil")
public class ProfilService extends HttpServlet {
	
	private static final long serialVersionUID = -7109249752388549689L;

	/** Récupérer un profil */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}
	
	/** Créer un profil */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}
	
	/** Modifie le profil */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}

}
