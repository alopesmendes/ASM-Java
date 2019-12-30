package fr.umlv.retro.features;

import java.util.List;
import java.util.function.Consumer;

import org.objectweb.asm.MethodVisitor;

public interface Feature {
	
	void rewriteFeature(int start, List< Consumer<MethodVisitor>> runs);
	
	static boolean detect(String origin, String feature) {
		return origin.equals(feature);
	}
}
