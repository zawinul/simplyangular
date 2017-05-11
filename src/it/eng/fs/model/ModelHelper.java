package it.eng.fs.model;

import it.eng.fs.util.Files;
import it.eng.fs.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;











import org.apache.commons.codec.binary.Base64;


public class ModelHelper {
	
	
	public static String xml(Model x) {
		try {
			JAXBContext c = JAXBContext.newInstance(x.getClass());
			Marshaller m =  c.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter w = new StringWriter();
			m.marshal(x, w);
			String s = w.toString();
			w.close();
			return s;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return ("ERROR: "+e.getMessage());
		}
	}

	public static String json(Model x) {
		return _json(x, false);
	}

	public static String jsonWithHiddenPassword(Model x) {
		return _json(x, true);
	}

	public static String jsonForLog(Model x) {
		try {
			JSONSerializer js = new JSONSerializer().prettyPrint(true).exclude("*.class");
			String json = js.deepSerialize(x);
			JSONObject j2 = new JSONObject(json);
			hidePassword(j2);
			truncate(j2);
			json = j2.toString(2);
			return json;
		} 
		catch (Exception e) {
			Log.util.debug(e);
			return ("ERROR: "+e.getMessage());
		}
	}

	

	public static String json(List<? extends Object> list) {
		if (list==null)
			return null;
		JSONArray a = new JSONArray();
		for(Object m:list) {
			if (m==null)
				a.put(JSONObject.NULL);
			else if (m instanceof Model)
				a.put(new JSONObject(json((Model) m)));
			else
				a.put(m);
		}
		return a.toString(2);
	}
	

	public static Object[] array(List<? extends Object> list) {
		if (list==null)
			return null;
		Object [] ret  = new Object[list.size()];
		for(int i=0;i<list.size();i++)
			ret[i] = list.get(i);
		return ret;
	}
	
	public static String jsonFormat(String json) {
		JSONObject o = new JSONObject(json);
		return o.toString(2);
	}
	
	private static String _json(Model x, boolean hiddenPassword) {
		try {
			JSONSerializer js = new JSONSerializer().prettyPrint(true).exclude("*.class");
			String json = js.deepSerialize(x);
			if (hiddenPassword) {
				JSONObject j2 = new JSONObject(json);
				hidePassword(j2);
				json = j2.toString(2);
			}
			return json;
		} 
		catch (Exception e) {
			Log.util.debug(e);
			return ("ERROR: "+e.getMessage());
		}
	}

//	public static String simpleJson(Model x) {
//		try {
//			JSONSerializer js = new JSONSerializer().exclude("*.class");
//			String json = js.deepSerialize(x);
//			
//			return json;
//		} 
//		catch (Exception e) {
//			Log.util.debug(e);
//			return ("ERROR: "+e.getMessage());
//		}
//	}
//
//	public static String simpleJsonString(Model x) {
//		try {
//			JSONSerializer js = new JSONSerializer().exclude("*.class");
//			String json = js.deepSerialize(x);
//			
//			return JSONObject.quote(json);
//		} 
//		catch (Exception e) {
//			Log.util.debug(e);
//			return ("ERROR: "+e.getMessage());
//		}
//	}

	
	public static <T> T createFromObject(Class<T> x, Object src) throws Exception {
		try {
			T t = x.newInstance();
			JSONSerializer js = new JSONSerializer().prettyPrint(true).exclude("*.class");
			String json = js.deepSerialize(src);
			JSONDeserializer<T> jd = new JSONDeserializer<T>();
			jd.deserializeInto(json, t);
			return t;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends Model> T  extend (T target, Model src) {
		return extend(target, json(src));
	}

	public static <T extends Model> T  extend (T target, String jsonSrc) {
		return extend(target, new StringReader(jsonSrc));
	}

	public static <T extends Model> T  extend (T target, InputStream is) {
		return extend(target, new InputStreamReader(is, Charset.forName("UTF-8")));
	}
	

	public static <T extends Model> T  extend (T target, File f) {
		try {
			InputStream is = new FileInputStream(f);
			extend(target, is);
			is.close();
		} catch (Exception e) {
			Log.util.warn("", e);
		} 
		return target;
	}
	
	public static <T extends Model> T  readResource(Class<T> x, String resourcePath) {
		T t;
		try {
			t = x.newInstance();
			return extend(t, x.getClassLoader().getResourceAsStream(resourcePath));
		} catch (Exception e) {
			Log.util.error("readResource", e);
			return null;
		}
	}
	
	public static String toString(Reader r) {
		StringBuffer b = new StringBuffer();
		final int BUFSIZE = 4096;
		char buf[] = new char[BUFSIZE];
		try {
			while(true) {
				int n = r.read(buf);
				if (n<=0)
					break;
				b.append(buf,0,n);
			}
			r.close();
		} 
		catch (IOException e) {
			Log.util.debug("", e);
		}
		return b.toString();
	}
	
	public static String toString(InputStream is) {
		String s = null;
		try {
			if (is==null)
				return null;
			
			s = toString(new InputStreamReader(is, Charset.forName("UTF-8")));
			is.close();
		}  
		catch (Exception e) {
			Log.util.debug("",e);
		}
		return s;
	}

	public static <T extends Model> T  extend (T target, Reader jsonSrc) {
		try {
			JSONObject jsrc = new JSONObject(new JSONTokener(jsonSrc));
			return extend(target, jsrc);
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		return target;
	}

	public static <T extends Model> T  extend (T target, JSONObject jsrc) {
		try {
			JSONObject jtarget = new JSONObject(json(target));
			JSONObject merged = mergeObjects(jtarget, jsrc);
			String j = merged.toString(2);
			JSONDeserializer<T> jd = new JSONDeserializer<T>();
			jd.deserializeInto(j, target);
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		return target;
	}

	public static <T extends Model> T  clone (T src) {
		if (src==null)
			return null;
		String serialized = json(src);
		try {
			@SuppressWarnings("unchecked")
			T t = (T) src.getClass().newInstance();
			extend(t, serialized);
			return t;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} 
	}
	
	public static <T extends Model> T fromJSON(String json, Class<T> modclass)  throws Exception {
		if (json==null)
			return null;
		T t = modclass.newInstance();
		return extend(t, json);
	}
	
	public static <T extends Model> T fromJSONFile(String filename, Class<T> modclass)  {
		try {
			String json = Files.readFileIntoString(filename);
			if (json==null)
				return null;
			return fromJSON(json, modclass);
		} catch (Exception e) {
			Log.util.debug("", e);
			return null;
		}
	}
	
	public static <T extends Model> boolean saveToJSONFile(T obj, String filename)  {
		try {
			FileWriter f = new FileWriter(filename);
			f.write(obj.toString());
			f.close();
			return true;
		} catch (Exception e) {
			Log.util.warn("", e);
			return false;
		}
	}
	
	public static String encodeBase64(byte [] data) {
		if (data==null)
			return null;
		byte[] out = Base64.encodeBase64(data);
		return new String(out, UTF8);
	}
	
	public static byte[] decodeBase64(String data) {
		if (data==null)
			return null;
		return Base64.decodeBase64(data.getBytes(UTF8));
	}

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssS");
	private static String datePattern = dateFormat.format(new Date(0L));
	private static int dpLen = datePattern.length();
	private static Charset UTF8 = Charset.forName("UTF-8");
	
	private static JSONObject mergeObjects(JSONObject base, JSONObject merge) throws Exception {
		// Clone the initial object (JSONObject doesn't support "clone").
		String [] names = JSONObject.getNames(base);
		JSONObject returned = (names!=null)
				? new JSONObject(base, JSONObject.getNames(base))
				: new JSONObject();
		
		// Walk parameter list for the merged object and merge recursively.
		String[] fields = JSONObject.getNames(merge);
		if (fields==null)
			return returned;
		
		for (String field : fields) {
			Object existing = returned.opt(field);
			Object mergeField = merge.get(field);
			if (existing==null || returned.isNull(field))  {
				//if (mergeField!=null)
					returned.put(field, mergeField);
			} 
			else if (mergeField == null) {
				// it's removing a pre-configured value.
				returned.put(field, mergeField);
			} 
			else {
				if (mergeField instanceof JSONObject && existing instanceof JSONObject) {
					returned.put(field, mergeObjects((JSONObject)existing, (JSONObject)mergeField));
				} else {
					// Otherwise we just overwrite it.
					returned.put(field, mergeField);
				}
			}
		}
		return returned;
	}



	private static void hidePassword(JSONObject base) throws Exception {
		String pwd = base.optString("password");
		if (pwd != null && (!pwd.equals(""))) {
			String ret = "*****";
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				byte [] d = digest.digest(pwd.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < d.length; ++i) {
		        	sb.append(Integer.toHexString((d[i] & 0xFF) | 0x100).substring(1,3));
		        }
		        ret = "obscured, MD="+sb.toString();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			base.put("password", ret);
		}
		for(String name: JSONObject.getNames(base)) {
			JSONObject o = base.optJSONObject(name);
			if (o!=null)
				hidePassword(o);
		}
	}

	private static void truncate(JSONObject base) throws Exception {
		final int DIM = 500;
		String [] names = JSONObject.getNames(base);
		if (names==null)
			return;
		for(String name: names) {
			String s = base.optString(name);
			if (name.equals("base64content") || name.equals("html")) {
				if (s!=null && s.length()>500)
					base.put(name, s.substring(0,DIM)+"...");
			}
			Object o = base.opt(name);
			if (o!=null && (o instanceof JSONObject))
				truncate((JSONObject) o);
		}
	}

	public static String date2String(Date d) {
		if (d==null)
			return "null";
		else
			return dateFormat.format(d);
	}

	public static Date string2Date(String s) {
		if (s==null)
			return null;
		int len = s.length();
		if (len > dpLen)
			s = s.substring(0, dpLen);
		else if (len < dpLen)
			s = s+datePattern.substring(len);
		try {
			return dateFormat.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
