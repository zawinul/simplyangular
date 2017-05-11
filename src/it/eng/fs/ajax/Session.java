package it.eng.fs.ajax;

import it.eng.fs.model.Configuration;
import it.eng.fs.model.ConfigurationHelper;
import it.eng.fs.model.Model;

public class Session  extends AjaxHandler {
	
	public static class Logout extends AjaxFunction {
		public boolean ok = true;
		@Override
		public void run() throws Exception {
			req.getSession().invalidate();
		}
	}
	public static class GetConfiguration extends AjaxFunction {
		
		public Conf conf = new Conf();
		
		@Override
		public void run() throws Exception {
			Configuration config = ConfigurationHelper.get(); 
			conf = new Conf();
			conf.debug = config.isDebug;
			conf.appName = config.appName;		
		}
	}
	
	public static class Conf extends Model {
		public boolean debug;
		public String appName;
	}
	
	
	
}
