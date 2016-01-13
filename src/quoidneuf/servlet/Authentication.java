package quoidneuf.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/authentication")
public class Authentication extends HttpServlet {
	
	private static final long serialVersionUID = 7229118350560492306L;

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
	}

}
