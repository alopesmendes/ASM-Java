package fr.umlv.retro.features;

import java.util.stream.IntStream;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class Concat  implements Feature {
	private final Type[] argumentsTypes;
	private final String[] array;
	private final MethodVisitor mv;
	private int index;
	private int start;
	
	private Concat(int start, MethodVisitor mv, Type[] argumentsTypes, String[] array) {
		this.argumentsTypes = argumentsTypes;
		this.array = array;		
		this.mv = mv;
		this.start = start;
	}
	
	public static Concat create(int start, MethodVisitor mv, String descriptor, Object... bootstrapMethodArguments) {
		Type[] types = Type.getArgumentTypes(descriptor);
		return new Concat(start, mv, types, bootstrapMethodArguments[0].toString().split("(?=(?!^)\u0001)|(?<=\u0001)"));
	}
	
	
	private void store(int i) {
		int sort = argumentsTypes[i].getSort();
		if (sort == Type.BOOLEAN || sort == Type.INT || sort == Type.CHAR || sort == Type.SHORT || sort == Type.BYTE) {
			mv.visitVarInsn(Opcodes.ISTORE, start+i);
		} else if (sort == Type.LONG) {
			mv.visitVarInsn(Opcodes.LSTORE, start+i*2);
		} else if (sort == Type.FLOAT) {
			mv.visitVarInsn(Opcodes.FSTORE, start+i);
		} else if (sort == Type.DOUBLE) {
			mv.visitVarInsn(Opcodes.DSTORE, start+i*2);
		} else { mv.visitVarInsn(Opcodes.ASTORE, start+i); }
	}
	
	private void load(int i) {
		int sort = argumentsTypes[i].getSort();
		if (sort == Type.BOOLEAN || sort == Type.INT || sort == Type.CHAR || sort == Type.SHORT || sort == Type.BYTE) {
			mv.visitVarInsn(Opcodes.ILOAD, start+i);
		} else if (sort == Type.LONG) {
			mv.visitVarInsn(Opcodes.LLOAD, start+i*2);
		} else if (sort == Type.FLOAT) {
			mv.visitVarInsn(Opcodes.FLOAD, start+i);
		} else if (sort == Type.DOUBLE) {
			mv.visitVarInsn(Opcodes.DLOAD, start+i*2);
		} else { mv.visitVarInsn(Opcodes.ALOAD, start+i); }
	}
	 
	private void begin() {
		IntStream.range(0, argumentsTypes.length).forEach(i -> store(argumentsTypes.length-1-i));
		mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(StringBuilder.class));
		mv.visitInsn(Opcodes.DUP);
		try {
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(StringBuilder.class),
					"<init>", 
					Type.getConstructorDescriptor(StringBuilder.class.getConstructor()), false);
		} catch (NoSuchMethodException | SecurityException e) { throw new AssertionError(e); }
		
	}
	
	
	
	private void append(Type t) {
		if (!(t.equals(Type.INT_TYPE) || t.equals(Type.BOOLEAN_TYPE) || t.equals(Type.BYTE_TYPE) || t.equals(Type.LONG_TYPE) ||
			t.equals(Type.DOUBLE_TYPE) || t.equals(Type.FLOAT_TYPE) || t.equals(Type.CHAR_TYPE) || t.equals(Type.getType(String.class)))) {
			t = Type.getType(Object.class);
		}
		String des = Type.getMethodDescriptor(Type.getType(StringBuilder.class), t);
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, 
				Type.getInternalName(StringBuilder.class),
				"append", des, false);
		
	}

	private void end() {
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, 
				Type.getInternalName(StringBuilder.class),
				"toString", "()Ljava/lang/String;", false);
		
	}


	private void ldc(String text) {
		mv.visitLdcInsn(text);
	}
	
	private void apply(String text) {
		if (text.equals("\u0001")) {
			load(index);
			append(argumentsTypes[index++]);
		} else {
			ldc(text);
			append(Type.getType(String.class));
		}
	}
	
	private void buildString() {
		IntStream.range(0, array.length).forEach(i -> apply(array[i]));
	}

	public void execute() {
		begin();
		buildString();
		end();
		
		
	}
	
}
