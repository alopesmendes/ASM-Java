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
		System.out.println("je visite method : " + name);

		if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
			handles.add(new Handle(Opcodes.H_GETFIELD, classOwner, name, descriptor, false));
		}
		return cv.visitMethod(access, name, descriptor, signature, exceptions);
	}

	@Override
	public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
		System.out.println("je visite champs : " + name);
		if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
			handles.add(new Handle(Opcodes.H_GETFIELD, Type.getType(descriptor).getInternalName(), name, descriptor, false));
		}
		return cv.visitField(access, name, descriptor, signature, value);
	}
	
	@Override
	public void visitEnd() {
		System.out.println(handles.stream().map(e -> e.toString()).collect(Collectors.joining(", ", "[", "]")));
		cv.visitEnd();
	}
}
