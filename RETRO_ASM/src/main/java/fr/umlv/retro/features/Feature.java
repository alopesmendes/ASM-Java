package fr.umlv.retro.features;

import org.objectweb.asm.ClassVisitor;

public interface Feature {
	
	/**
	 * @param origin
	 * @param feature
	 * @return true if origin equals feature.
	 */
	static boolean detect(String origin, String feature) {
		return origin.equals(feature);
	}
	
	static ClassVisitor createWriterFeauture(int api, ClassVisitor classVisitor) {
		ClassVisitor visitor = new Nestmates(api, classVisitor);
		visitor = new Concat(api, visitor);
		visitor = new Record(api, visitor);
		return visitor;
	}
	
}
