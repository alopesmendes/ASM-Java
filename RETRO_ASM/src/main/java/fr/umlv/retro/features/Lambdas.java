package fr.umlv.retro.features;

import org.objectweb.asm.ClassVisitor;

public class Lambdas extends ClassVisitor implements Feature {

	public Lambdas(int api, ClassVisitor classVisitor) {
		super(api, classVisitor);
	}



}
