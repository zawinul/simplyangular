package it.eng.fs.web;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import it.eng.fs.model.ConfigurationHelper;
import it.eng.fs.util.Log;
import it.eng.fs.web.upload.UploadServlet;

public class FSSessionListener implements HttpSessionListener {
	static int count = 0;
	
	public void sessionCreated(HttpSessionEvent arg0) {
		Log.web.debug("\n#####    SESSIONSTARTED "+count+", id="+arg0.getSession().getId());
		arg0.getSession().setAttribute("sessionCount", count);
		count++;
		
		ConfigurationHelper.refresh();
	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		Log.web.debug("\n#####    SESSION END"+arg0.getSession().getAttribute("sessionCount")+", id="+arg0.getSession().getId());
		UploadServlet.cleanSession(arg0.getSession().getId());
	}
	
}