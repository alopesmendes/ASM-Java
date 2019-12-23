package fr.umlv.retro.features;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Concat implements Feature {
	private final String className;
	private final String methodDescriptor;
	private final int line;
	private final String[] args;
	/**
	 * @param className
	 * @param line
	 * @param args
	 */
	private Concat(String className, String methodDescriptor, int line, String[] args) {
		if (line < 0) {
			throw new IllegalArgumentException("number line < 0");
		}
		this.className = Objects.requireNonNull(className);
		this.methodDescriptor = Objects.requireNonNull(methodDescriptor);
		this.line = line;
		this.args = Objects.requireNonNull(args);
	}
	
	public static Concat create(String className, String method, int line, String ...args) {
		String[] array = className.split("/");
		String cName = Arrays.stream(array).skip(array.length-1).findFirst().get();
		return new Concat(cName, method, line, args);
	}
	
	private static String convert(String arg) {
		return arg.equals("\u0001") ? "%1" : arg;
	}
	
	@Override
	public String toString() {
		return 	"CONCATENATION at "+className+"."+methodDescriptor+
				" ("+className+".java:"+line+"): pattern "+
				Arrays.stream(args).map(Concat::convert).
				collect(Collectors.joining());
	}
	
}
