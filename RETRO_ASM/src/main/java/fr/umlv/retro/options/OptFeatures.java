package fr.umlv.retro.options;


import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OptFeatures implements Option {
	private final HashMap<String,Integer> map;
	
	/**
	 * @param args
	 */
	private OptFeatures(List<String> args) {
		this.map = applyArgs(args);
	}
	
	private static HashMap<String, Integer> initMapFeatures() {
		HashMap<String, Integer>map = new HashMap<String, Integer>();
		map.put("TryWithRessources", 0);
		map.put("Lambda",0);
		map.put("Concatenation",0);
		map.put("Nestmates",0);
		map.put("Record",0);
		return map;
	}

	private static List<String> getArgs(List<String> args) {
		Objects.requireNonNull(args);
		if (args.size()==0) throw new IllegalArgumentException();
		int last = 0;
		while (last< (args.size()-1) && args.get(last).endsWith(",")) {
			last++;
		}
		last++;
		return args.subList(0, last).stream().filter(x -> !(x.startsWith("-"))).map(x -> x.replace(",", "")).collect(Collectors.toList());	
	}
	
	private static HashMap<String, Integer> applyArgs(List<String> args){
		var res = initMapFeatures();
		var new_args = getArgs(args);
		for (String string : new_args) {
			res.computeIfPresent(string, (k,v)-> 1);
		}
		return res;
	}

	@Override
	public void execute() {
		map.forEach((k,v)->System.out.println("Feature " + k + " || Activated : " + v));
	}
	
	public static Option create(List<String> args) {
		Option res = new OptFeatures(args);
		return res;
	}

}
