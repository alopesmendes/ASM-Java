package fr.umlv.retro.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class Concat implements Feature {
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
	private int k;
	
	private Concat(Type[] types, List<String> elements) {
		this.types = types;
		this.elements = new ArrayList<>(elements);
	}
	
	
	public static Concat create(String description, String s) {
		Type[] types = Type.getArgumentTypes(description);
		List<String> list = Arrays.stream(s.split("\u0001"))
				.filter(x -> !x.isEmpty()).collect(Collectors.toList());
		System.out.println(list);
		return new Concat(types, list);
	}
	
	private void consume(List<Consumer<MethodVisitor>> runs, int index, int i) {
		/*index = Math.min(runs.size()-1, index);
		k = index;
		Consumer<MethodVisitor> consumer = runs.get(index);
		if (elements.get(i).equals("\u0001")) {
			runs.set(index, consumer.andThen(append.apply(types[j])));
			j++;
		} else {
			Consumer<MethodVisitor> ldc = mv -> mv.visitLdcInsn(elements.get(i));
			runs.set(k, runs.get(k).andThen(ldc).andThen(append.apply(Type.getType(String.class))));
			k= index;
		}*/
		Consumer<MethodVisitor> consumer = runs.get(index);
		Consumer<MethodVisitor> ldc = mv -> mv.visitFieldInsn(Opcodes.GETFIELD, types[i].getInternalName(), "a"+index, types[i].getDescriptor());
		runs.set(index, consumer.andThen(append.apply(types[i])));
		
	}
	
	@Override
	public void rewriteFeature(int start, List<Consumer<MethodVisitor>> runs) {
		int s = runs.size() - types.length;
		Consumer<MethodVisitor> c2 = runs.get(s);
		runs.set(s, begin.andThen(c2));
		//runs.set(s, runs.get(s).andThen(append.apply(Type.INT_TYPE)));
		IntStream.range(s, runs.size()).forEach(i -> {
			//consume(runs, s+1+j, i);
			consume(runs, i, i-s);
		});
		runs.add(end);
		
	}

	
	
	
}
