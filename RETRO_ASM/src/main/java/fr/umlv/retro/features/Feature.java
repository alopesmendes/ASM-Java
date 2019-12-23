package fr.umlv.retro.features;

public interface Feature {
	
	
	static boolean detect(String origin, String feature) {
		return origin.equals(feature);
	}
}
