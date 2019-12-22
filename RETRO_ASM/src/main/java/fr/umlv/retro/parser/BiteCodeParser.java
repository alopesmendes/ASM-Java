package fr.umlv.retro.parser;


public class BiteCodeParser {
	
	public static int getVersion(byte[] bytes) {
		if (bytes.length<8) {
			throw new  IllegalArgumentException();
		}
		int majorversion = Integer.parseInt(String.format("%02X",bytes[7]));
		return ((majorversion/10)*16 + majorversion%10) - 44;
		
	}
}
