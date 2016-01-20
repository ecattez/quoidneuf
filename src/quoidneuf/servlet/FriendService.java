package quoidneuf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/friends")
@ServletSecurity(@HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"user", "super-user", "admin"}))
public class FriendService extends HttpServlet {
	
	private static final long serialVersionUID = -7109249752388549689L;
	
	/** Récupérer un ou plusieurs amis */
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}
	
	/** Faire une demande d'ami */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}
	
	/** Confirmer une demande d'ami */
	public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}
	
	/** Supprimer d'un lien d'amitié (confirmé ou non) */
	public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}

}
