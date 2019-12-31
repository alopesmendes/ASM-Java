package fr.umlv.retro.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class Concatenation implements Feature {
	private final Consumer<MethodVisitor> begin = mv -> {
		mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(StringBuilder.class));
		mv.visitInsn(Opcodes.DUP);
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(StringBuilder.class), "<init>", "()V", false);
	};
	
	private final Function<Type, Consumer<MethodVisitor>> append = t -> {
		return mv -> {	 
			String des = Type.getMethodDescriptor(Type.getType(StringBuilder.class), t);
			mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, 
				Type.getInternalName(StringBuilder.class),
				"append", des, false);
		};
	};
	
	private final Consumer<MethodVisitor> end = mv -> {
		mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, 
				Type.getInternalName(StringBuilder.class),
				"toString", "()Ljava/lang/String;", false);
		};
			
	private final ArrayList<String> elements;
	private final Type[] types;
	private int j;
	
	private Concatenation(Type[] types, List<String> elements) {
		this.types = types;
		this.elements = new ArrayList<>(elements);
	}
	
	
	public static Concatenation create(String description, String s) {
		Type[] types = Type.getArgumentTypes(description);
		List<String> list = Arrays.stream(s.split(""))
				.collect(Collectors.toList());
		System.out.println(list);
		return new Concatenation(types, list);
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
		System.out.println(sb);
		k= j == elements.size() ? k : k-1;
		runs.set(k, runs.get(k).andThen(ldc(sb.toString())));
	}
	
	private void consume(List<Consumer<MethodVisitor>> runs) {
		int k = (runs.size() - types.length);
		while (j < elements.size()) {
			Consumer<MethodVisitor> c = runs.get(k);
			if (elements.get(j).equals("\u0001") || elements.get(j).equals("\u0002")) {
				runs.set(k, c.andThen(append.apply(types[k - (runs.size() - types.length)])));
				j++; k = Math.min(runs.size()-1, k+1);
			} else {
				constantString(k, runs);
			}
		}
		
	}
	
	@Override
	public void rewriteFeature(int start, List<Consumer<MethodVisitor>> runs) {
		
		runs.set(runs.size() - types.length-1, runs.get(runs.size() - types.length-1).andThen(begin));
		//IntStream.range(0, elements.size()).forEach(x -> consume(x, runs));
		consume(runs);
		runs.add(end);
		
	}

	
	
	
}
