package fr.umlv.retro.features;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
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
	
	public static FeatureVisitor create(ObserverVisitor observerVisitor, ParsingOptions...options) {
		return new FeatureVisitor(observerVisitor);
	}
	
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		//observerVisitor.visit(version, access, name, signature, superName, interfaces);
		cv.visit(version, access, name, signature, superName, interfaces);
	}
	
	private MethodVisitor rewriteFeatures(String n, MethodVisitor methodVisitor) {
		return new MethodVisitor(api, methodVisitor) {
			
			@Override
			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
					Object... bootstrapMethodArguments) {
				//mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
			}
		};
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		MethodVisitor mv = rewriteFeatures(name, cv.visitMethod(access, name, descriptor, signature, exceptions));
		mv = rewriteFeatures(name, mv);
		System.out.println(name+"{"+observerVisitor+"}");
		return mv;
	}
	
	@Override
	public void visitEnd() {
		cv.visitEnd();
		System.out.println("end:{\n"+observerVisitor+"\n}");
	}
	
	@Override
	public String toString() {
		return observerVisitor.toString();
	}
	
}