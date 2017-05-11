package it.eng.fs.ajax;

import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;
import it.eng.fs.model.Model;
import it.eng.fs.model.ModelHelper;
import it.eng.fs.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import java.util.Base64;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Element;

public abstract class AjaxFunction extends Model  {
	
	protected HttpServletRequest req;
	protected HttpServletResponse resp;
	protected SysInfo sysInfo = new SysInfo();
	
	public SysInfo getSysInfo() { 
		return sysInfo; 
	}

	public static AjaxFunction getStub() {
		AjaxFunction s = new AjaxFunction(){

			@Override
			public void run() throws Exception {
			}
		};
		return s;
	}
	
	public void addError(String txt) {
		if (sysInfo.error==null)
			sysInfo.error = new ArrayList<String>();
		sysInfo.error.add(txt);
	}
	
	public void  addWarning(String txt) {
		if (sysInfo.warning==null)
			sysInfo.warning = new ArrayList<String>();
		sysInfo.error.add(txt);
	}
	
	public void  returnCode(int status) {
		sysInfo.retCode = status;
	}

	
	protected void readInput() {
		String m = req.getMethod().toLowerCase();
		JSONObject postInput = null;
		if (m.equals("post")) {
			try {
				InputStream reqIs = req.getInputStream();
				if (reqIs!=null) {
					Reader r = new InputStreamReader(reqIs);
					StringWriter w = new StringWriter();
					char [] buf = new char[1024];
					while(true) {
						int n = r.read(buf);
						if (n<=0)
							break;
						w.write(buf,0,n);
					}
					String input = w.toString();
					w.close();
					r.close();
					if (input.trim().startsWith("%7B")) // probabilmente e' un oggetto JSON HTML-encoded (%7B = "{")
						input = StringEscapeUtils.unescapeHtml4(input);
					
					if (input.trim().startsWith("{")) { // probabilmente e' un oggetto JSON 
						postInput = new JSONObject(input);
						if (postInput.has("data") && postInput.keySet().size()==1) { // se ha solo la chiave data
							postInput = postInput.getJSONObject("data");
						}
					}
				}
			} 
			catch (Exception e) {
				Log.ajax.warn("", e);
			} 	
		}

		try {
			JSONObject params = new JSONObject();
			Enumeration<String> parNames = req.getParameterNames();
			while(parNames.hasMoreElements()) {
				String key = parNames.nextElement();
				params.put(key, req.getParameter(key));
			}
			ModelHelper.extend(this, params);
			if (postInput!=null)
				ModelHelper.extend(this, postInput);
			String s = req.getParameter("data");
			if (s!=null) 
				ModelHelper.extend(this, s);
		} catch (Exception e) {
			Log.ajax.warn(e);
		}
	}

	protected void writeOutput() {
		
		try {
			String json = serialize();
			resp.getWriter().write(json);
		} 
		catch (IOException e) {
			Log.ajax.warn(e);
		}
	}


	public void exec(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		this.req = req;
		this.resp = resp;
		
		readInput();
		run();
		writeOutput();
	};

	public static class BA extends AbstractTransformer  {
		@Override
		public void transform(Object object) {
			byte[] bytes = (byte[]) object; 
			//getEncoder().encodeToString(bytes);
			String base64 = new String(Base64.encodeBase64(bytes), Charset.forName("UTF-8"));
			getContext().writeQuoted(base64);
			
			
		}
	}
	
//	public static class BAback extends flexjson.ObjectFactory {
//		@Override
//		public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
//			return null;
//		}
//	}

	public static class P extends AbstractTransformer  {

		@Override
		public void transform(Object object) {
			if (object instanceof Calendar) {
				Calendar c = (Calendar) object;
				getContext().write(""+c.getTimeInMillis());
			}
			else if (object instanceof Element) {
				Element x = (Element) object;
				getContext().writeQuoted(x.toString());
			}			
//			else if (object instanceof AllegatiType) {
//				AllegatiType x = (AllegatiType) object;
//				byte [] bytes = x.getAttachment();
//				String base64 = Base64.getEncoder().encodeToString(bytes);
//				getContext().writeQuoted(x.toString());
//				JSONContext ctx = getContext();
//			}			
			else if (object instanceof JSONObject) {
				JSONObject x = (JSONObject) object;
				try {
					getContext().write(x.toString(2));
				} catch (JSONException e) {
					Log.ajax.warn(e);
				}
			}			
		}		
	} 

	public String serialize() {
		Hashtable<String, Object> map = new Hashtable<String, Object>();
	    for (Field f: this.getClass().getDeclaredFields()) {
	    	try {
				if (f.isAnnotationPresent(Transient.class))
					continue;
				Object v = f.get(this);
				if (v!=null)
					map.put(f.getName(), v);
			} 
	    	catch (Exception e) {
				//Log.ajax.warn(e);
			} 
	    }
		map.put("sysInfo", getSysInfo());
		P p = new P();
		BA ba = new BA();
		byte [] arr = new byte[0];
		
		JSONSerializer js = new JSONSerializer()
			.prettyPrint(true)
			.exclude("*.class")
			.exclude("*.size")
			.transform(p, Element.class)
			.transform(p, JSONObject.class)
			.transform(p, Calendar.class)
			.transform(ba, arr.getClass())
			;


		try {
			String k = js.deepSerialize(map);
			return k;
		} catch (Exception e) {
			Log.ajax.warn(e);
			return "{}";
		}
	}
	

	public abstract void run() throws Exception;
}