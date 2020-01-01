package fr.umlv.retro.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class Concat implements Feature {
	private Consumer<MethodVisitor> begin() {
		Consumer<MethodVisitor> b =  mv -> {
			mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(StringBuilder.class));
			mv.visitInsn(Opcodes.DUP);
		};
		return b.andThen(initBuilder());
	}
	
	private Consumer<MethodVisitor> initBuilder() {
		return mv -> {
			try {
				mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(StringBuilder.class),
						"<init>", 
						Type.getConstructorDescriptor(StringBuilder.class.getConstructor()), false);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new AssertionError(e);
			}
		};
	}
	
	private Consumer<MethodVisitor> append(Type t, int var) {
		return mv -> {
			String des = Type.getMethodDescriptor(Type.getType(StringBuilder.class), t);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, 
					Type.getInternalName(StringBuilder.class),
					"append", des, false);
		};
	}

	private Consumer<MethodVisitor> end() {
		return mv -> {
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, 
					Type.getInternalName(StringBuilder.class),
					"toString", "()Ljava/lang/String;", false);
		};
	}

	private final ArrayList<String> elements;
	private final Type[] types;
	private int j;

	private Concat(Type[] types, List<String> elements) {
		this.types = types;
		this.elements = new ArrayList<>(elements);
	}


	public static Concat create(String description, String s) {
		Type[] types = Type.getArgumentTypes(description);
		List<String> list = Arrays.stream(s.split(""))
				.collect(Collectors.toList());
		return new Concat(types, list);
	}

	private Consumer<MethodVisitor> ldc(String text) {
		return  mv -> {
			mv.visitLdcInsn(text);
			String des = Type.getMethodDescriptor(Type.getType(StringBuilder.class), Type.getType(String.class));
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, 
					Type.getInternalName(StringBuilder.class),
					"append", des, false);
		};
	}


	private void constantString(int k, List<Consumer<MethodVisitor>> runs) {
		StringBuilder sb = new StringBuilder();
		for (; j < elements.size() && !elements.get(j).equals("\u0001") && !elements.get(j).equals("\u0002"); j++) {
			sb.append(elements.get(j));
		}
		k= j == elements.size()  ? k : k-1;
		runs.set(k, runs.get(k).andThen(ldc(sb.toString())));
	}

	private void consume(List<Consumer<MethodVisitor>> runs) {
		int k = (runs.size() - types.length);
		//runs.set(k, initBuilder().andThen(runs.get(k)));
		while (j < elements.size()) {
			Consumer<MethodVisitor> c = runs.get(k);
			if (elements.get(j).equals("\u0001") || elements.get(j).equals("\u0002")) {
				int var = k - (runs.size() - types.length);
				runs.set(k, c.andThen(append(types[var], runs.size()-1)));
				j++; k = Math.min(runs.size()-1, k+1);
			} else { constantString(k, runs); }
		}
	}

	@Override
	public void rewriteFeature(int start, List<Consumer<MethodVisitor>> runs) {
		runs.add(runs.size() - types.length, begin());

		consume(runs);
		runs.add(end());

	}




}
