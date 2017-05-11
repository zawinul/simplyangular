package it.eng.fs.model;

import java.io.File;
import java.io.InputStream;

import it.eng.fs.util.Log;

public class ConfigurationHelper {

	public static Configuration get() {
		if (singleton==null) {
			singleton = new Configuration();
			InputStream is = ConfigurationHelper.class.getClassLoader().getResourceAsStream("configuration.json");
			if (is!=null)
				ModelHelper.extend(singleton, is);
			if (System.getProperty("configurationPatch")!=null)
				ModelHelper.extend(singleton, new File(System.getProperty("configurationPatch")));
			Log.util.debug("CONFIG: "+singleton);
		}
		return singleton;
	}

	
	public static void refresh() {
		singleton = null;
	}
	
	private static Configuration singleton = null;
}
