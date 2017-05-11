package it.eng.fs.web.upload;


import it.eng.fs.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class ContentUploader {
	
	private long fileLengh = 0;
	private String serverId = null;
	private boolean state=true;
	private boolean verificato=false;
	
	public boolean getVerificato() { return verificato;}
	
	public long getLength() {return fileLengh;} 
	public String getServerId() {return serverId;} 
	public boolean getState() { return state;}
	private static String TEMPDIR = "c:/temp/";
	public static byte[] getSavedData(String id) {
		
		try {
			File f=new File(TEMPDIR+id);
			FileInputStream fis = new FileInputStream(f);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte buf[]=new byte[4096];
			int blen;
			while((blen=fis.read(buf))>0) {
				bos.write(buf,0,blen);
			}
			bos.flush();
			byte [] a = bos.toByteArray();
			bos.close();
			fis.close();
			return a;
		} catch (Exception e) {
			Log.web.error(e);
			return ("Errore: "+e.getMessage()).getBytes();
		}
		
	}
	
	public boolean saveOnTempDir(InputStream filecontent, String name)  {
		
		try {
			serverId = UUID.randomUUID()+"."+name;
			File f=new File(TEMPDIR+serverId);
			OutputStream fout=new FileOutputStream(f);
			byte buf[]=new byte[4096];
			int blen;
			while((blen=filecontent.read(buf))>0) {
				fout.write(buf,0,blen);
				fileLengh += blen;
			}
			fout.close();

			return true;
		} 
		catch (Exception e) {
			return false;
		} 
	}
	
}
