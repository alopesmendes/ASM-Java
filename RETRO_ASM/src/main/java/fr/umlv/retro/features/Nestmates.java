package fr.umlv.retro.features;

import java.util.ArrayList;
import java.util.stream.IntStream;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * The Nestmates class will rewrite the Nestmates.
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public class Nestmates extends ClassVisitor implements Feature {
	private String classOwner;
	private final ArrayList<String> nests = new ArrayList<>();

	public Nestmates(int api, ClassVisitor classVisitor) {
		super(api, classVisitor);
	}

	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		classOwner = name;
		cv.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public void visitNestHost(String nestHost) {
		nests.add(nestHost);
	}

	@Override
	public void visitNestMember(String nestMember) {
		nests.add(nestMember);
	}
	
	/**
	 * @param methodVisitor
	 * @return
	 */
	private MethodVisitor remplaceNest(MethodVisitor methodVisitor) {
		return new MethodVisitor(api, methodVisitor) {


			/**
			 * @param opcode
			 * @param name
			 * @return
			 */
			private String methodName(int opcode, String owner, String name) {
				if (opcode == Opcodes.GETFIELD || opcode == Opcodes.GETSTATIC || opcode == Opcodes.INVOKEVIRTUAL || opcode == Opcodes.INVOKESTATIC) {
					return "$get$" + name.substring(0, 1).toUpperCase() + name.substring(1);
				} else { return "$set$" + name.substring(0, 1).toUpperCase() + name.substring(1); }
			}
			
			/**
			 * @param opcode
			 * @param methodName
			 * @param owner
			 * @param descriptor
			 */
			private void methodInsn(int opcode, String methodName, String owner, String descriptor) {
				if (opcode == Opcodes.GETFIELD || opcode == Opcodes.PUTFIELD) {
					descriptor = opcode == Opcodes.GETFIELD ? "()"+descriptor : "("+descriptor+")V";
					mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, owner, methodName, descriptor, false);
				} else if (opcode == Opcodes.GETSTATIC || opcode == Opcodes.PUTSTATIC) {
					descriptor = opcode == Opcodes.GETSTATIC ? "()"+descriptor : "("+descriptor+")V";
					mv.visitMethodInsn(Opcodes.INVOKESTATIC, owner, methodName, descriptor, false);
				}
			}

			@Override
			public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
				
				if (!classOwner.equals(owner) && nests.contains(owner)) {
					String methodName = methodName(opcode, owner, name);
					methodInsn(opcode, methodName, owner, descriptor);
					return;
				}
				mv.visitFieldInsn(opcode, owner, name, descriptor);
			}
			
			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
				if (!classOwner.equals(owner) && nests.contains(owner) && 
				!name.equals("<init>") && !name.equals("main") && !name.equals("<clinit>")) {
					name = "$"+name;
					isInterface = false;
				}
				mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
			}
		};
	}
	
	/**
	 * @param mv
	 * @param access
	 * @param name
	 * @param descriptor
	 */
	private void invoke(MethodVisitor mv, int access, String name, String descriptor) {
		int opcode = Opcodes.INVOKEVIRTUAL;
		if ((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC) {
			opcode = Opcodes.INVOKESTATIC;
		}
		mv.visitMethodInsn(opcode, classOwner, name, descriptor, false);
	}
	
	/**
	 * @param access
	 * @param name
	 * @param descriptor
	 * @param signature
	 * @param exceptions
	 */
	private void createMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(publicAccessOf(access), "$"+name, descriptor, signature, exceptions);
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		Type[] types = Type.getArgumentTypes(descriptor);
		IntStream.range(0, types.length).forEach(i -> mv.visitVarInsn(types[i].getOpcode(Opcodes.ILOAD), i + 1));
		invoke(mv, access, name, descriptor);
		mv.visitInsn(Type.getReturnType(descriptor).getOpcode(Opcodes.IRETURN));
		mv.visitMaxs(0, 0);
		
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
		if (!nests.isEmpty()) {
			if (!name.equals("<init>") && !name.equals("main") && !name.equals("<clinit>")) {
				createMethod(access, name, descriptor, signature, exceptions);
			}
			return remplaceNest(mv);
		} else { return mv;}
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		if (!nests.isEmpty()) {
			get(access, name, descriptor);
			if (!((access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL)) { set(access, name, descriptor); }
		}
		
		return cv.visitField(access, name, descriptor, signature, value);
	}

	/**
	 * @param access
	 * @return
	 */
	private int publicAccessOf(int access) {
		if ((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC) {
			return Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC;
		}
		return Opcodes.ACC_PUBLIC;
	}
	
	/**
	 * @param mv
	 * @param name
	 * @param descriptor
	 */
	private void createStaticGetter(MethodVisitor mv, String name, String descriptor) {
		mv.visitFieldInsn(Opcodes.GETSTATIC, classOwner, name, descriptor);
	}
	
	/**
	 * @param mv
	 * @param name
	 * @param descriptor
	 */
	private void createGetter(MethodVisitor mv, String name, String descriptor) {
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitFieldInsn(Opcodes.GETFIELD, classOwner, name, descriptor);	
	}

	/**
	 * @param access
	 * @param name
	 * @param descriptor
	 */
	private void get(int access, String name, String descriptor) {
		String methodName = "$get$" + name.substring(0, 1).toUpperCase() + name.substring(1);
		MethodVisitor mv = cv.visitMethod(publicAccessOf(access), methodName, "()" + descriptor, null, null);
		if (((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC)) {
			createStaticGetter(mv, name, descriptor);
		} else { createGetter(mv, name, descriptor); }
		mv.visitInsn(Type.getType(descriptor).getOpcode(Opcodes.IRETURN));
		mv.visitMaxs(0, 0);
	}
	
	/**
	 * @param mv
	 * @param name
	 * @param descriptor
	 */
	private void createStaticSetter(MethodVisitor mv, String name, String descriptor) {
		mv.visitVarInsn(Type.getType(descriptor).getOpcode(Opcodes.ILOAD), 0);
		mv.visitFieldInsn(Opcodes.PUTSTATIC, classOwner, name, descriptor);
	}
	
	/**
	 * @param mv
	 * @param name
	 * @param descriptor
	 */
	private void createSetter(MethodVisitor mv, String name, String descriptor) {
		mv.visitVarInsn(Opcodes.ALOAD, 0);
		mv.visitVarInsn(Type.getType(descriptor).getOpcode(Opcodes.ILOAD), 1);
		mv.visitFieldInsn(Opcodes.PUTFIELD, classOwner, name, descriptor);
	}
	
	/**
	 * @param access
	 * @param name
	 * @param descriptor
	 */
	private void set(int access, String name, String descriptor) {
		String methodName = "$set$" + name.substring(0, 1).toUpperCase() + name.substring(1);
		MethodVisitor mv = cv.visitMethod(publicAccessOf(access), methodName, "("+descriptor+")V", null, null);
		if (((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC)) {
			createStaticSetter(mv, name, descriptor);
		} else { createSetter(mv, name, descriptor);}
		mv.visitInsn(Opcodes.RETURN);
		mv.visitMaxs(0, 0);
	}

	@Override
	public void visitEnd() {
		cv.visitEnd();
	}
}
