package br.com.gilbertopapa.bluekeeper.util;

public class StringUtils {

	public static final boolean isEmpty(String str) {
		if (str == null) {
			return true;
		}
		
		return str.trim().length() == 0;
	}
	
	public static String newLine() {
		return System.getProperty("line.separator");
	}
}
