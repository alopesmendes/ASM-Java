package fr.umlv.retro.features;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
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
	public FeatureVisitor(int version, ClassWriter cw,  ObserverVisitor observerVisitor) {
		super(Opcodes.ASM7, cw);
		this.observerVisitor = observerVisitor;
		this.version = version;
	}
	
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		observerVisitor.visit(version, access, name, signature, superName, interfaces);
		cv.visit(this.version, access, name, signature, superName, interfaces);
	}
	
	private MethodVisitor rewriteFeatures(String n, MethodVisitor ov, MethodVisitor methodVisitor) {
		return new MethodVisitor(api, methodVisitor) {
			private final ArrayList<Consumer<MethodVisitor>> list = new ArrayList<>();
			private int start;
			
			@Override
			public void visitCode() {
				
				ov.visitCode();
				list.add(mv -> mv.visitCode());
			}
			
			@Override
			public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
				list.add(mv -> mv.visitAnnotableParameterCount(parameterCount, visible));
			}
			
			@Override
			public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
				list.add(mv -> visitMultiANewArrayInsn(descriptor, numDimensions));
			}
			
			@Override
			public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
				list.add(mv -> visitTryCatchBlock(start, end, handler, type));
			}
			
			@Override
			public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
				
				list.add(mv -> visitTableSwitchInsn(min, max, dflt, labels));
			}
			
			@Override
			public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
				list.add(mv -> visitLookupSwitchInsn(dflt, keys, labels));
			}
			
			@Override
			public void visitAttribute(Attribute attribute) {
				ov.visitAttribute(attribute);
				list.add(mv -> mv.visitAttribute(attribute));
			}
			
			@Override
			public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
				ov.visitFrame(type, numLocal, local, numStack, stack);
				list.add(mv -> visitFrame(type, numLocal, local, numStack, stack));
			}
			
			@Override
			public void visitJumpInsn(int opcode, Label label) {
				ov.visitJumpInsn(opcode, label);
				list.add(mv -> mv.visitJumpInsn(opcode, label));
			}
			
			@Override
			public void visitVarInsn(int opcode, int var) {
				ov.visitVarInsn(opcode, var);
				list.add(mv -> mv.visitVarInsn(opcode, var));
			}
			
			@Override
			public void visitTypeInsn(int opcode, String type) {
				ov.visitTypeInsn(opcode, type);
				list.add(mv -> mv.visitTypeInsn(opcode, type));
			}
			
			@Override
			public void visitLdcInsn(Object value) {
				ov.visitLdcInsn(value);
				list.add(mv -> mv.visitLdcInsn(value));
			}
			
			@Override
			public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end,
					int index) {
				ov.visitLocalVariable(name, descriptor, signature, start, end, index);
				list.add(mv -> mv.visitLocalVariable(name, descriptor, signature, start, end, index));
			}
			
			@Override
			public void visitIincInsn(int var, int increment) {
				ov.visitIincInsn(var, increment);
				list.add(mv -> mv.visitIincInsn(var, increment));
			}
			
			@Override
			public void visitIntInsn(int opcode, int operand) {
				ov.visitIntInsn(opcode, operand);
				
				list.add(mv -> mv.visitIntInsn(opcode, operand));
			}
			
			@Override
			public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
				ov.visitFieldInsn(opcode, owner, name, descriptor);				
				list.add(mv -> mv.visitFieldInsn(opcode, owner, name, descriptor));
			}
			
			@Override
			public void visitInsn(int opcode) {
				ov.visitInsn(opcode);
				list.add(mv -> mv.visitInsn(opcode));
			}
			
			@Override
			public void visitLineNumber(int line, Label start) {
				ov.visitLineNumber(line, start);
				list.add(mv -> mv.visitLineNumber(line, start));
			}
			
			@Override
			public void visitLabel(Label label) {
				list.add(mv -> mv.visitLabel(label));
			}
			
			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
				ov.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
				list.add(mv -> mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface));				
			}
			
			@Override
			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
					Object... bootstrapMethodArguments) {
				ov.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
				Concatenation c = Concatenation.create(descriptor, bootstrapMethodArguments[0].toString());
				c.rewriteFeature(start, list);
				//list.add(m -> m.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments));
			}
			
			@Override
			public void visitMaxs(int maxStack, int maxLocals) {
				list.add(mv -> mv.visitMaxs(maxStack, maxLocals));
			}
			
			@Override
			public void visitEnd() {
				ov.visitEnd();
				IntStream.range(0, list.size()).forEach(i -> list.get(i).accept(mv));	
				mv.visitEnd();
			}
		};
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		MethodVisitor ov = observerVisitor.visitMethod(access, name, descriptor, signature, exceptions);
		MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
		
		return rewriteFeatures(name, ov, mv);
	}
	
	@Override
	public String toString() {
		return observerVisitor.toString();
	}
	
}
