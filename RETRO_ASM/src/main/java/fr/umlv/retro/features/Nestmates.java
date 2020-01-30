package fr.umlv.retro.features;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class Nestmates extends ClassVisitor implements Feature {
	private final List<Handle> handles = new ArrayList<>();
	private String classOwner;
	
	public Nestmates(int api, ClassVisitor classVisitor) {
		super(api, classVisitor);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		classOwner = name;
		cv.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
			handles.add(new Handle(Opcodes.H_GETFIELD, classOwner, name, descriptor, false));
		}
		return cv.visitMethod(access, name, descriptor, signature, exceptions);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
			handles.add(new Handle(Opcodes.H_GETFIELD, Type.getType(descriptor).getInternalName(), name, descriptor, false));
			createGetter(name, descriptor);
		}
		return cv.visitField(access, name, descriptor, signature, value);
	}
	
	void createGetter(String name, String descriptor) {
		  String methodName = "$get" + name.substring(0, 1).toUpperCase() 
		      + name.substring(1);
		  MethodVisitor mv = 
		      cv.visitMethod(Opcodes.ACC_PUBLIC, methodName, "()" + descriptor, null, null);
		  mv.visitVarInsn(Opcodes.ALOAD, 0);
		  mv.visitFieldInsn(Opcodes.GETFIELD, classOwner, name, descriptor);
		  mv.visitInsn(Type.getType(descriptor).getOpcode(Opcodes.IRETURN));
		  mv.visitMaxs(0, 0);
	}
	
	@Override
	public void visitEnd() {
		System.out.println(handles.stream().map(e -> e.toString()).collect(Collectors.joining(", ", "[", "]")));
		cv.visitEnd();
	}
}
