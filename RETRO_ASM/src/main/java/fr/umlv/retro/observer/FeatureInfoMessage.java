package fr.umlv.retro.observer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

import fr.umlv.retro.features.Concatenation;
import fr.umlv.retro.features.Feature;
import fr.umlv.retro.features.Lambdas;
import fr.umlv.retro.features.Nestmates;
import fr.umlv.retro.features.Record;
import fr.umlv.retro.features.TryWithRessources;

public class FeatureInfoMessage {
	private final HashMap<Class<? extends Feature>, Function<String[], String>> map;
	
	private FeatureInfoMessage(HashMap<Class<? extends Feature>, Function<String[], String>> map) {
		this.map = map;
	}
	
	private static String convert(String arg) {
		return arg.equals("\u0001") ? "%1" : arg;
	}
	
	private static String className(String name) {
		String[] array = name.split("/");
		return Arrays.stream(array).skip(array.length-1).findFirst().get();
	}
	
	private static Function<String[], String> createConcatenationMessage() {
		return (args) -> {
			String cName = className(args[0]);
			String arg = Arrays.stream(args[3].split("")).map(FeatureInfoMessage::convert).
						collect(Collectors.joining());
			return "CONCATENATION at " + cName + "." + 
					args[1] + " ("+cName+".java:"+args[2]+") pattern "+ arg;
		};
	}
	
	private static Function<String[], String> createLambdaMessage() {
		return (args) -> {
			String cName = className(args[0]);
			String type = Type.getReturnType(args[3]).getInternalName();
			String capture = Arrays.stream(Type.getArgumentTypes(args[3])).map(t -> t.getInternalName()).
							collect(Collectors.joining());	
			String l = args[4].substring(0, args[4].length() - 4);
			return "LAMBDA at " + cName + "." + args[1] + 
					" ("+cName+".java:"+args[2]+") lambda "+type+" capture ["+capture+"] calling "
					+l.replaceFirst(args[0], cName);
		};
	}
	
	private static Function<String[], String> createTryWithRessourcesMessage() {
		return (args) -> {
			String cName = className(args[0]);
			String owner = args[3].contains(cName) ? className(args[3]) : args[3];
			return "TRY_WITH_RESOURCES at " + cName + "." + args[1] + " ("+
					cName+".java:"+args[2]+") try-with-resources on "+owner;
		};
	}
	
	private static Function<String[], String> createNesmateMessage() {
		return (args) -> {
			String arg1 = className(args[0]);
			String arg2 = className(args[1]);
			String arg3 = className(args[3]);
			return 	"NESTMATES at " + arg1 + " ("+arg2+".java):"+args[2]+
					" "+arg3;

		};
	}
	
	private static Function<String[], String> createRecordMessage() {
		return (args) -> {
			String cName = className(args[0]);
			String cRecord = className(args[1]);
			return "RECORD at " + cName + " ("+cName+".java): record of " + cRecord;
		};
	}
	
	public static FeatureInfoMessage create() {
		HashMap<Class<? extends Feature>, Function<String[], String>> map = new HashMap<>();
		map.put(Concatenation.class, createConcatenationMessage());
		map.put(Lambdas.class, createLambdaMessage());
		map.put(TryWithRessources.class, createTryWithRessourcesMessage());	
		map.put(Nestmates.class, createNesmateMessage());
		map.put(Record.class, createRecordMessage());
		
		return new FeatureInfoMessage(map);
	}
	
	public String infoOf(Class<? extends Feature> type, String... args) {
		return map.get(type).apply(args);
	}
	
	
}
