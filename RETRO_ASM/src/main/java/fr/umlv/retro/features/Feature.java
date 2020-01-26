package fr.umlv.retro.features;

public interface Feature {
	
	/**
	 * @param origin
	 * @param feature
	 * @return true if origin equals feature.
	 */
	static boolean detect(String origin, String feature) {
		return origin.equals(feature);
	}
}
