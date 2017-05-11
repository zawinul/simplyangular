package it.eng.fs.web.upload;


import it.eng.fs.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	
	//ENG1927
	public static class Item {
		public String sessionId;
		public byte data[];
		public String contentType;
		public String fileName;
	}
	
	static Map<String, Item> slist = new HashMap<String, UploadServlet.Item>();
	
	public static void selfUpload(String key, String contentType, String fileName, String sessionId, byte data[]) {
		Item item = new Item();
		item.contentType = contentType;
		item.fileName = fileName;
		item.data = data;
		item.sessionId = sessionId;
		Log.web.debug("salvo file upload su static, key="+key);
		slist.put(key, item);
	}
	
	private static void addStaticItem(String sessionId,String key,FileItem item) throws Exception {
		if (slist.get(key)!=null) {
			Log.web.debug("rimuovo item "+key+" per sovrascrittura");
			slist.remove(key);
		}
		Item si = new Item();
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		InputStream is = item.getInputStream();
		
		byte buf[] = new byte[10000];
		while(true) {
			int n = is.read(buf);
			if (n<=0)
				break;
			b.write(buf,0,n);
		}
		
		si.data = b.toByteArray();
		si.contentType = item.getContentType();
		si.fileName = item.getName();
		si.sessionId = sessionId;
		slist.put(key, si);
	}
	
	public static Item getUploaded(String key) {
		if (key==null)
			return null;
		Item s = slist.get(key);
		//return (s!=null) ? s.item : null;	
		return s;	
	}

	public static void cleanSession(String sessionId) {
		for(String key: slist.keySet()) {
			Item item = slist.get(key);
			if (item.sessionId==sessionId) {
				Log.web.debug("delete item "+key+" (session end)");
				//item.item.delete();
				slist.remove(key);
			}
		}
	}
	//ENG1927
	
	
	
	public static class SavedItem implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public String filename;
		public String serverId;
		
		public SavedItem(String filename, String serverId) {
			this.filename = filename;
			this.serverId = serverId;
		}
	}

	@Override
	public void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, java.io.IOException {
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
        resp.addHeader("Access-Control-Allow-Method", "GET, PUT, POST, OPTIONS, X-XSRF-TOKEN");
	};

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException{
		doIt("GET", req, resp);
	}
		
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException{
		doIt("POST", req, resp);
	}
	
	public void doIt(String method, HttpServletRequest req, HttpServletResponse resp) throws ServletException{
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.addHeader("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
        resp.addHeader("Access-Control-Allow-Method", "GET, PUT, POST, OPTIONS, X-XSRF-TOKEN");
		
        String staticKey = req.getParameter("saveOnStatic");
        String session = req.getParameter("sessionId");
		FileItem fileItem = null;
        try {
			ServletFileUpload uploader = new ServletFileUpload(new DiskFileItemFactory());
			List<FileItem> items = uploader.parseRequest(req);
			for(FileItem item:items) {
				if (item.isFormField()) {
					String name = item.getFieldName();
					if(name.equals("saveOnStatic")){
						staticKey = item.getString();
					}
					if(name.equals("sessionId")){
						session = item.getString();
					}
				}
				else
					fileItem = item;
			}
			if (staticKey!=null && fileItem!=null && session!=null) {
				Log.web.debug("salvo file upload su static, key="+staticKey);
				addStaticItem(session, staticKey, fileItem);
			}
			else {
				Log.web.info("upload senza effetti");
			}
		} 
		catch (Exception e) {
			throw new ServletException("Cannot parse multipart request.", e);
		}
	}
	

}
	
