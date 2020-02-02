package fr.umlv.retro.features;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import fr.umlv.retro.features.Concat.ConcatImpl;

/**
 * The Record class will rewrite the Records.
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public class Record extends ClassVisitor implements Feature {
	private String className;
	public Record(int api, ClassVisitor classVisitor) {
		super(api, classVisitor);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		cv.visit(version, access, name, signature, Type.getInternalName(Object.class), interfaces);
		className = name;		
	}
	
	private static void getField(int var, Handle handle, MethodVisitor mv) {
		mv.visitVarInsn(Opcodes.ALOAD, var);
		mv.visitFieldInsn(Opcodes.GETFIELD, handle.getOwner(), handle.getName(), handle.getDesc());
	}
	
	private static List<Handle> handles(Object... bootstrapMethodArguments) {
		return 	IntStream.range(2, bootstrapMethodArguments.length).
				mapToObj(i -> (Handle)bootstrapMethodArguments[i]).collect(Collectors.toList());
	}
	
	private MethodVisitor rewriteHashCode(MethodVisitor methodVisitor) {
		return new MethodVisitor(api, methodVisitor) {
			
			private Type primitifOwner(Handle handle) {
				switch (Type.getType(handle.getDesc()).getSort()) {
					case Type.LONG:		return Type.getType(Long.class);
					case Type.DOUBLE:	return Type.getType(Double.class);
					case Type.BOOLEAN:	return Type.getType(Boolean.class);
					case Type.BYTE:		return Type.getType(Byte.class);
					case Type.CHAR: 	return Type.getType(Character.class);
					case Type.FLOAT:	return Type.getType(Float.class);
					case Type.SHORT:	return Type.getType(Short.class);
					default: return Type.getType(handle.getDesc());
				}
			}
			
			private void ixor(Handle handle) {
				getField(0, handle, mv);
				Type owner = primitifOwner(handle);
				Type t = Type.getType(handle.getDesc());
				if (!owner.equals(t)) {
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, owner.getInternalName(), "hashCode", Type.getMethodDescriptor(Type.INT_TYPE, t), handle.isInterface());
				} else if (!Type.INT_TYPE.equals(t)) {
					mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, t.getInternalName(), "hashCode", "()I", handle.isInterface());
				}
				mv.visitInsn(Opcodes.IXOR);
				
			}
			
			private void load(Object... bootstrapMethodArguments) {
				List<Handle> handles = handles(bootstrapMethodArguments);
				getField(0, handles.get(0), mv);
				handles.stream().skip(1).forEach(h -> ixor(h));
			}
			
			@Override
			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
					Object... bootstrapMethodArguments) {
				load(bootstrapMethodArguments);
				//mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
			}
		};
	}
	
	private MethodVisitor rewriteEquals(MethodVisitor methodVisitor) {
		return new MethodVisitor(api, methodVisitor) {
		
			
			private void instanceOf(String owner, List<Handle> handles) {
				mv.visitInsn(Opcodes.POP2);
				mv.visitVarInsn(Opcodes.ALOAD, 1);
				mv.visitTypeInsn(Opcodes.INSTANCEOF, owner);
				Label label = new Label();
				mv.visitJumpInsn(Opcodes.IFNE, label);
				mv.visitInsn(Opcodes.ICONST_0);
				mv.visitInsn(Opcodes.IRETURN);
				mv.visitLabel(label);
				mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			}
			
			private void loadVar(Handle handle) {
				getField(2, handle, mv);
				getField(0, handle, mv);
			}
			
			private void equalsOfVar(Label label, Handle handle) {
				loadVar(handle);
				Type t = Type.getType(handle.getDesc());
				if (t.equals(Type.INT_TYPE) || t.equals(Type.BOOLEAN_TYPE) || t.equals(Type.BYTE_TYPE) || t.equals(Type.LONG_TYPE) ||
					t.equals(Type.DOUBLE_TYPE) || t.equals(Type.FLOAT_TYPE) || t.equals(Type.CHAR_TYPE)) {
					mv.visitJumpInsn(Opcodes.IF_ICMPNE, label);
					return;
				}
				mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, Type.getType(handle.getDesc()).getInternalName(), "equals", "(Ljava/lang/Object;)Z", false);
				mv.visitJumpInsn(Opcodes.IFEQ, label);
			}
			
			private void buildEquals(Label label, String owner, List<Handle> handles) {
				mv.visitVarInsn(Opcodes.ALOAD, 1);
				mv.visitTypeInsn(Opcodes.CHECKCAST, owner);
				mv.visitVarInsn(Opcodes.ASTORE, 2);
				handles.stream().forEach(h -> equalsOfVar(label, h));
			}
			
			private void end(String owner, Label label) {
				Label end = new Label();
				Object[] locals = {owner};
				
				mv.visitInsn(Opcodes.ICONST_1);
				mv.visitJumpInsn(Opcodes.GOTO, end);
				mv.visitLabel(label);
				mv.visitFrame(Opcodes.F_APPEND, 1, locals, 0, null);
				mv.visitInsn(Opcodes.ICONST_0);
				mv.visitLabel(end);
				mv.visitFrame(Opcodes.F_SAME1, 0, locals, 1, new Object[]{1});
				
			}
			
			
			
			@Override
			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
					Object... bootstrapMethodArguments) {
				Label label = new Label();
				String owner = Type.getType(bootstrapMethodArguments[0].toString()).getInternalName();
				List<Handle> handles = handles(bootstrapMethodArguments);
				instanceOf(owner, handles);
				buildEquals(label, owner, handles);
				end(owner, label);
				//mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
			}
		};
	}
	
	private MethodVisitor rewriteToString(MethodVisitor methodVisitor) {
		return new MethodVisitor(api, methodVisitor) {
			
			
			private String className() {
				String[] array = className.split("/");
				return array[array.length-1];
			}
			
			public void firstInsn(Handle handle) {
				ConcatImpl.ldc(className()+"["+handle.getName()+"=", mv);
				getField(0, handle, mv);
				ConcatImpl.append(Type.getType(handle.getDesc()), mv);
			}
			
			private void build(Object...bootstrapMethodArguments) {
				List<Handle> handles = IntStream.range(2, bootstrapMethodArguments.length).
				mapToObj(i -> (Handle)bootstrapMethodArguments[i]).collect(Collectors.toList());
				firstInsn(handles.get(0));
				handles.stream().skip(1).forEach(h -> {
					ConcatImpl.ldc(", "+h.getName()+"=", mv);
					getField(0, h, mv);
					ConcatImpl.append(Type.getType(h.getDesc()), mv);
				});
				ConcatImpl.ldc("]", mv);
				
			}
			
			@Override
			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
					Object... bootstrapMethodArguments) {
				ConcatImpl.begin(mv);
				build(bootstrapMethodArguments);
				ConcatImpl.end(mv);
				//mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
			}
			
		};
	} 
	
	private MethodVisitor rewriteInit(MethodVisitor methodVisitor) {
		return new MethodVisitor(api, methodVisitor) {
			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
				mv.visitMethodInsn(opcode, Type.getInternalName(Object.class), name, descriptor, isInterface);
			}
		};
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
		if (name.equals("equals")) {
			return rewriteEquals(mv);
		} else if (name.equals("toString")) {
			return rewriteToString(mv);
		} else if (name.equals("<init>")) {
			return rewriteInit(mv);
		} else if (name.equals("hashCode")) {
			return rewriteHashCode(mv);
		}
		return mv;
	}
}
