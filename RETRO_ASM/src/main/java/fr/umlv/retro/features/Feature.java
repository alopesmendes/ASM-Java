package fr.umlv.retro.features;

import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.MethodVisitor;

public interface Feature {
	
	/**
	 * Rewrites a feature with a starting point and runs.
	 * @param start
	 * @param runs
	 */
	void rewriteFeature(int start, List< Consumer<MethodVisitor>> runs);
	
	/**
	 * @param origin
	 * @param feature
	 * @return true if origin equals feature.
	 */
	static boolean detect(String origin, String feature) {
		return origin.equals(feature);
	}
}
