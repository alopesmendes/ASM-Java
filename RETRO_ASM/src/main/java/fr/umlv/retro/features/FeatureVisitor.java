package fr.umlv.retro.features;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import fr.umlv.retro.observer.ObserverVisitor;
import fr.umlv.retro.parser.ParsingOptions;

public class FeatureVisitor extends ClassVisitor {
	private final ObserverVisitor observerVisitor;

	/**
	 * Constructs a FeatureVisitor with it's version, ClassWriter and ObserverVisitor.
	 * @param api
	 * @param observerVisitor
	 */
	private FeatureVisitor(ObserverVisitor observerVisitor) {
		super(Opcodes.ASM7, observerVisitor);
		this.observerVisitor = observerVisitor;
	}
	
	/**
	 * Creates a featureVisitor.
	 * @param observerVisitor
	 * @param options
	 * @return the featureVisitor.
	 */
	public static FeatureVisitor create(ClassWriter cw, ParsingOptions...options) {
		return new FeatureVisitor(new ObserverVisitor(cw));
	}
	
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		cv.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		
		return cv.visitMethod(access, name, descriptor, signature, exceptions);
	}
	
	@Override
	public void visitEnd() {
		cv.visitEnd();
	}
	
	@Override
	public String toString() {
		return observerVisitor.toString();
	}
	
}