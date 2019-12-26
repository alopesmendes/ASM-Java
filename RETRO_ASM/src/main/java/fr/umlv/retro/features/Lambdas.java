package fr.umlv.retro.features;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

public class Lambdas implements Feature {
	private final String className;
	private final String methodDescriptor;
	private final String descriptor;
	private final int line;
	private final String lambdaDescriptor;
	
	/**
	 * @param className
	 * @param methodDescriptor
	 * @param line
	 * @param args
	 */
	private Lambdas(String className, String methodDescriptor, String descriptor, String lambdaDescriptor,  int line) {
		if (line < 0) {
			throw new IllegalArgumentException("line < 0");
		}
		this.className = Objects.requireNonNull(className);
		this.methodDescriptor = Objects.requireNonNull(methodDescriptor);
		this.descriptor = Objects.requireNonNull(descriptor);
		this.line = line;
		this.lambdaDescriptor = Objects.requireNonNull(lambdaDescriptor);
	}
	
	
	public static Lambdas create(String className, String methodDescriptor, String descriptor, String lambdaDescriptor, int line) {
		String l = lambdaDescriptor.substring(0, lambdaDescriptor.length() - 4);
		String[] array = className.split("/");
		String cName = Arrays.stream(array).skip(array.length-1).findFirst().get();
		l = l.replaceFirst(className, cName);
		return new Lambdas(cName, methodDescriptor, descriptor, l,  line);
	}
	
	@Override
	public String toString() {
		String type = Type.getReturnType(descriptor).getInternalName();
		String capture = Arrays.stream(Type.getArgumentTypes(descriptor)).map(t -> t.getInternalName()).
						collect(Collectors.joining());	
		return 	"LAMBDA at "+className+"."+methodDescriptor+
				" ("+className+".java:"+line+"): lambda "+
				type+" capture ["+capture+"] calling "+ lambdaDescriptor;
	}
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(className, methodDescriptor, descriptor, lambdaDescriptor, line);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Lambdas)) {
			return false;
		}
		Lambdas lambda = (Lambdas) obj;
		return 	lambda.line == line &&
				lambda.className.equals(className) && lambda.methodDescriptor.equals(methodDescriptor) &&
				lambda.descriptor.equals(descriptor) && lambda.lambdaDescriptor.equals(lambdaDescriptor);
	}
		
	
}
