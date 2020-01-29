package fr.umlv.retro.features;

import java.util.Optional;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import fr.umlv.retro.observer.ObserverVisitor;
import fr.umlv.retro.options.OptionParsing;

public class FeatureVisitor extends ClassVisitor {
	private final ObserverVisitor observerVisitor;
	private final Optional<Integer> version;

	/**
	 * Constructs a FeatureVisitor with it's version, ClassWriter and ObserverVisitor.
	 * @param api
	 * @param observerVisitor
	 */
	private FeatureVisitor(ObserverVisitor observerVisitor, Optional<Integer> version) {
		super(Opcodes.ASM7, observerVisitor);
		this.observerVisitor = observerVisitor;
		this.version = version;
	}
	
	/**
	 * Creates a featureVisitor.
	 * @param observerVisitor
	 * @param options
	 * @return the featureVisitor.
	 */
	public static FeatureVisitor create(ClassWriter cw, OptionParsing options) {
		ClassVisitor classVisitor = cw;
		if (true) {
			classVisitor = Feature.createWriterFeauture(Opcodes.ASM7, classVisitor);
		}
		return new FeatureVisitor(new ObserverVisitor(classVisitor), options.version());
	}
	
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		cv.visit(this.version.orElse(version), access, name, signature, superName, interfaces);
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
		return getObserverVisitor().toString();
	}

	/**
	 * @return the observerVisitor.
	 */
	public ObserverVisitor getObserverVisitor() {
		return observerVisitor;
	}
	
}