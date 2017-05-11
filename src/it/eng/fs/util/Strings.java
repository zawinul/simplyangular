package it.eng.fs.util;

public class Strings {
	
	public static String getFromTo(String src, String from, String to, boolean inclusive) {
		return getFromTo(src, from, to, inclusive, null);
	}
	
	public static String getFromTo(String src, String from, String to, boolean inclusive, String defaultValue) {
	

		if (src==null)
			return defaultValue;
		int p1 = src.indexOf(from);
		if (p1<0)
			return defaultValue;
		src = src.substring(p1+from.length());
		int p2 = src.indexOf(to);
		if (p2<0)
			return defaultValue;
		src = src.substring(0, p2);
		if (inclusive)
			src = from+src+to;
		return src;
	}

	public static String decimaleITA(double x) {
		if (x<0)
			return "-"+decimaleITA(-x);
		if (x==0)
			return "0,00";
		
		x = Math.floor(x*100+.5);
		long xi = (long) x;
		long a = xi/100;
		long b = xi%100;
		return f(a) + "," + (""+(100+b)).substring(1);
	}
	

	private static String f(long x) {
		if (x==0)
			return "0";
		if (x<1000)
			return ""+x;
		String left = f(x/1000);
		String right = ""+(1000+x%1000);
		return left+"."+right.substring(1);
	}
}
