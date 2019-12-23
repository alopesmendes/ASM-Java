package fr.umlv.retro.features;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Nestmates implements Feature {
	private final String className;
	private final String host;
	private final List<String> members;
	/**
	 * @param className
	 * @param host
	 * @param members
	 */
	private Nestmates(String className, String host, List<String> members) {
		this.className = Objects.requireNonNull(className);
		this.host = Objects.requireNonNull(host);
		this.members = Objects.requireNonNull(members);
	}
	
	
	
	public static Nestmates create(String className, String host, List<String> members) {
		String[] array = className.split("/");
		String cName = Arrays.stream(array).skip(array.length-1).findFirst().get();
		return new Nestmates(cName, host, members);
	}
	
	public static Nestmates create(String className, String member) {
		return create(className, member, List.of());
	}
	
	private String hostOrMember() {
		String s = "NESTMATES at ";
		if (members.isEmpty()) {
			return 	s+className+" ("+host+".java): nestmate of "+host;
		}
		return 	s+className+" ("+className+".java): nest host "+className+" members "+
				members.stream().collect(Collectors.joining(", ", "[", "]"));
	}
	
	@Override
	public String toString() {
		return 	hostOrMember();
	}

}
