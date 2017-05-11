package it.eng.fs.ajax;


import it.eng.fs.util.Log;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void init(ServletConfig cfg) {
		AjaxHandler.initMapping();
	}

	@Override
	public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
		resp.addHeader("Access-Control-Allow-Origin", "*");
		//resp.addHeader("Access-Control-Allow-Origin", "localhost:9010");
		resp.addHeader("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
        resp.addHeader("Access-Control-Allow-Method", "GET, PUT, POST, OPTIONS, X-XSRF-TOKEN");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
	};

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException{
		doGet(req,resp);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException{
		String cmd = req.getRequestURI();
		cmd = cmd.substring(cmd.indexOf("/ajax/")+6);
		Log.ajax.debug("ajax: "+cmd);
		resp.setContentType("application/json;charset=utf8");

		resp.addHeader("Access-Control-Allow-Origin", "*");
		//resp.addHeader("Access-Control-Allow-Origin", "localhost:9010");
		resp.addHeader("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
        resp.addHeader("Access-Control-Allow-Method", "GET, PUT, POST, OPTIONS, X-XSRF-TOKEN");
        resp.addHeader("Access-Control-Allow-Credentials", "true");

		AjaxHandler.exec(cmd, req, resp);
	}
	

}

	
