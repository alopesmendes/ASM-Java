package fr.umlv.retro.features;

import java.util.Arrays;
import java.util.Objects;

public class TryWithRessources implements Feature {
	private final String className;
	private final String methodDescriptor;
	private final String onMethod;
	private final int line;
	/**
	 * @param className
	 * @param methodDescriptor
	 * @param onMethod
	 * @param line
	 */
	private TryWithRessources(String className, String methodDescriptor, String onMethod, int line) {
		if (line < 0) {
			throw new IllegalArgumentException("line < 0");
		}
		this.className = Objects.requireNonNull(className);
		this.methodDescriptor = Objects.requireNonNull(methodDescriptor);
		this.onMethod = Objects.requireNonNull(onMethod);
		this.line = line;
	}
	
	public static TryWithRessources create(String className, String methodDescriptor, String onMethod, int line) {
		String[] array = className.split("/");
		String cName = Arrays.stream(array).skip(array.length-1).findFirst().get();
		return new TryWithRessources(cName, methodDescriptor, onMethod, line);
	}
	
	@Override
	public String toString() {
		return "TRY-WITH-RESSOURCES at "+className+"."+methodDescriptor+
				" ("+className+".java:"+line+"): try-with-ressources on "+onMethod;
	}
	
}