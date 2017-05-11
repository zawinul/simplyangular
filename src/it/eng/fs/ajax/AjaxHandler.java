package it.eng.fs.ajax;

import it.eng.fs.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public abstract class AjaxHandler {

	public static void initMapping() {
		registerHandler(Session.class, "session");		
		registerHandler(Report.class, "report");		
	}

	public static void exec(String cmd, HttpServletRequest req, HttpServletResponse resp)  {
		
		resp.setCharacterEncoding("utf8");
		
		Class<? extends AjaxFunction> c = map.get(cmd);
		if (c==null) {
	    	AjaxFunction f = AjaxFunction.getStub();
	    	f.addError("handler non trovato: "+cmd);
	    	try {
				f.exec(req, resp);
			} 
	    	catch (Exception e) {
				Log.ajax.warn(e);
			}
		}
		else {
			try {
				AjaxFunction f = c.newInstance();
				f.exec(req,resp);
			} 
			catch (Exception e) {
				reportUnhandledException(e, cmd, req, resp);
			}
		}		
	}
	
	
	private static Hashtable<String, Class<? extends AjaxFunction>> map = new Hashtable<String, Class<? extends AjaxFunction>>(); 

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void registerHandler(Class<? extends AjaxHandler> c, String prefix) {
		for(Class f:c.getDeclaredClasses()) {
			if (AjaxFunction.class.isAssignableFrom(f)) {
				String key = f.getName();
				if (key.indexOf('$')>=0) {
					key = key.substring(key.indexOf('$')+1);
				}
				key = key.substring(0,1).toLowerCase()+key.substring(1);
				key = prefix+"/"+key;
				Log.ajax.debug("registering "+key);
				map.put(key, f);
			}
		}		
	}
	
    public static class ReportUnhandledException extends AjaxFunction {
    	public String stacktrace;
    	public String message;
    	public String cmd;
		@Override
		public void run() throws Exception {
		} 
    	
    }
    
    private static void reportUnhandledException(Exception ex, String cmd, HttpServletRequest req, HttpServletResponse resp) {
    	ReportUnhandledException r = new ReportUnhandledException();
    	r.req = req;
    	r.resp = resp;
    	try {

    		StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            pw.flush();
//            if (Config.get().debugExceptions) {
//	            r.stacktrace = sw.toString();
//	            r.message = ex.getMessage();
//            }
            r.cmd = cmd;
            r.returnCode(-9999);
           	r.addError("eccezione di sistema: "+ex.getMessage());
            r.exec(req, resp);
        } 
        catch (Exception e) {
            Log.ajax.warn(e);
        }      	
    }
    
}
