package fr.umlv.retro.features;

import org.objectweb.asm.ClassVisitor;

/**
 * The Lambdas class will rewrite the lambdas.
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public class Lambdas extends ClassVisitor implements Feature {

	public Lambdas(int api, ClassVisitor classVisitor) {
		super(api, classVisitor);
	}



}
