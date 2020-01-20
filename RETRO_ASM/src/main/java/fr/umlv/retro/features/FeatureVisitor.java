package fr.umlv.retro.features;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
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
	private FeatureVisitor(ClassWriter classWriter, ObserverVisitor observerVisitor) {
		super(Opcodes.ASM7, classWriter);
		this.observerVisitor = observerVisitor;
	}
	
	/**
	 * Creates a featureVisitor.
	 * @param observerVisitor
	 * @param options
	 * @return the featureVisitor.
	 */
	public static FeatureVisitor create(ClassWriter cw, ObserverVisitor observerVisitor, ParsingOptions...options) {
		return new FeatureVisitor(cw, observerVisitor);
	}
	
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		observerVisitor.visit(version, access, name, signature, superName, interfaces);
		cv.visit(version, access, name, signature, superName, interfaces);
	}
	
	private MethodVisitor rewriteFeatures(String n, MethodVisitor methodVisitor) {
		return new MethodVisitor(api, methodVisitor) {
			
			
			@Override
			public void visitCode() {
				mv.visitCode();
			}
			
			@Override
			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
					Object... bootstrapMethodArguments) {
				mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
			}
			
			@Override
			public void visitEnd() {
				mv.visitEnd();
			}
		};
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		MethodVisitor mv = rewriteFeatures(name, observerVisitor.visitMethod(access, name, descriptor, signature, exceptions));
		mv = rewriteFeatures(name, cv.visitMethod(access, name, descriptor, signature, exceptions));
		
		return mv;
	}
	
	@Override
	public void visitEnd() {
		observerVisitor.visitEnd();
		cv.visitEnd();
		System.out.println(observerVisitor);
	}
	
	@Override
	public String toString() {
		return observerVisitor.toString();
	}
	
}