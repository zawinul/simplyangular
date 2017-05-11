package it.eng.fs.web.download;

import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;


	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException{
		doPost(req, resp);
	}

	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException{

		try {
			String command = req.getParameter("command");	
			if (command==null)
				throw new ServletException("command==null in download servlet");
			if (command.equals("esempio")) {
				byte out[]  = "ciao, sono un esempio".getBytes();
				resp.setContentType("text/plain");
				resp.setHeader("Content-Disposition","attachment; filename=esempio.txt");
				resp.setContentLength(out.length);
				OutputStream ouputStream = resp.getOutputStream();
				ouputStream.write(out);
				ouputStream.flush();
				ouputStream.close();
			}
		}catch (Exception e) {			
			throw new ServletException(e);
		} 
	}
	
}
	
