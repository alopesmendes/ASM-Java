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
		observerVisitor.visit(version, access, name, signature, superName, interfaces);
		cv.visit(this.version, access, name, signature, superName, interfaces);
	}
	
	private MethodVisitor rewriteFeatures(MethodVisitor methodVisitor) {
		return new MethodVisitor(Opcodes.ASM7, methodVisitor) {
			
			@Override
			public void visitCode() {
				mv.visitCode();
			}
			
			@Override
			public void visitTypeInsn(int opcode, String type) {
				mv.visitTypeInsn(opcode, type);
			}
			
			@Override
			public void visitLdcInsn(Object value) {
				mv.visitLdcInsn(value);
			}
			
			@Override
			public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
				mv.visitFieldInsn(opcode, owner, name, descriptor);
			}
			
			
			
			@Override
			public void visitInsn(int opcode) {
				mv.visitInsn(opcode);
			}
			
			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
				mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
				
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
		
		MethodVisitor mv = observerVisitor.visitMethod(access, name, descriptor, signature, exceptions);
		System.out.println("{\n"+observerVisitor+"\n}");
		
		return rewriteFeatures(mv);
	}
	
	@Override
	public String toString() {
		return observerVisitor.toString();
	}
	
}
