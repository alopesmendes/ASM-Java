package fr.umlv.retro.features;

import org.objectweb.asm.ClassVisitor;

public class TryWithRessources extends ClassVisitor implements Feature {

	public TryWithRessources(int api, ClassVisitor classVisitor) {
		super(api, classVisitor);
	}

	
}