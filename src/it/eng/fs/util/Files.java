package it.eng.fs.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

public class Files {
	
	public static String streamToString(InputStream stream) {
		byte data[] = streamToByteArray(stream);
		if (data==null)
			return null;
		return new String(data, Charset.forName("UTF-8"));
	}
	
	public static String readFileIntoString(String filename) {
		try {
			InputStream is = new FileInputStream(filename);
			byte data[] = streamToByteArray(is);
			is.close();
			if (data==null)
				return null;
			return new String(data, Charset.forName("UTF-8"));
		} 
		catch (Exception e) {
			Log.util.warn("", e);
			return null;
		} 
	}
	
	public static byte[] streamToByteArray(InputStream stream) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buf[] = new byte[4096]; 
			while(true) {
				int n = stream.read(buf);
				if (n<=0)
					break;
				baos.write(buf,0,n);
			}
			//stream.close();
			byte filedata[] = baos.toByteArray();
			baos.close();
			return filedata;
		} catch (IOException e) {
			Log.util.debug("", e);
			return null;
		}

	}
}
