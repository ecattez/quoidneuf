package quoidneuf.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

public abstract class JsonServlet extends HttpServlet {

	private static final long serialVersionUID = 3796747675330297774L;
	
	public void sendJson(HttpServletResponse res, Object json) throws IOException {
		PrintWriter out = (PrintWriter) res.getWriter();
		res.setContentType("application/json");
		out.println(json);
	}


}
