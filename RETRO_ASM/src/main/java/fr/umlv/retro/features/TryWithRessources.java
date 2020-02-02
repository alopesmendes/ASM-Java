package fr.umlv.retro.features;

import org.objectweb.asm.ClassVisitor;

/**
 * The TryWithRessources class will rewrite the try-with-resources.
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public class TryWithRessources extends ClassVisitor implements Feature {

	public TryWithRessources(int api, ClassVisitor classVisitor) {
		super(api, classVisitor);
	}

	
}