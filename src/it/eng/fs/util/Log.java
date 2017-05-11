package it.eng.fs.util;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Log {
	
	public static Logger web;
	public static Logger db;
	public static Logger config;
	public static Logger ajax;
	public static Logger sap;
	public static Logger util;
	// ....
	static {
		reinit();
	}
	
	public static void reinit() {
		// cerca in varie directory dove potrebbe stare log4j.properties
		String pfiles[] = {
			"log4j.properties",
			"C:/temp/log4j.properties.test"
		};
		try {
			for(String f:pfiles) {
				InputStream is = Log.class.getClassLoader().getResourceAsStream(f);
				Properties p = new Properties();
				p.load(is);
				if (is!=null) {
					System.out.println(" CONFIGURING LOG FROM "+f);
					PropertyConfigurator.configure(p);
					break;
				}
			}
			for(String f:pfiles) {
				if (new File(f).exists()) {
					System.out.println(" CONFIGURING LOG FROM "+f);
					PropertyConfigurator.configure(f);
					break;
				}
			}
								
			web  = Logger.getLogger("web");
			ajax = Logger.getLogger("ajax");
			sap  = Logger.getLogger("sap");
			db  = Logger.getLogger("db");
			util = Logger.getLogger("util");
			config = Logger.getLogger("conf");

			Logger loggers[] =  new Logger[] { web, sap, ajax, util,db };
			for(Logger l: loggers)
				l.setAdditivity(false);
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 	
	}
	
}
