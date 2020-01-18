package fr.umlv.retro.features;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import fr.umlv.retro.observer.ObserverVisitor;

public class FeatureVisitor extends ClassVisitor {
	private final ObserverVisitor observerVisitor;
	private final int version;

	/**
	 * Constructs a FeatureVisitor with it's version, ClassWriter and ObserverVisitor.
	 * @param api
	 * @param observerVisitor
	 */
	public FeatureVisitor(int version, ObserverVisitor observerVisitor) {
		super(Opcodes.ASM7, observerVisitor);
		this.observerVisitor = observerVisitor;
		this.version = version;
	}
	
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		//observerVisitor.visit(version, access, name, signature, superName, interfaces);
		cv.visit(this.version, access, name, signature, superName, interfaces);
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