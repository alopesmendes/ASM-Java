package fr.umlv.retro.options;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


import fr.umlv.retro.features.Concat;
import fr.umlv.retro.features.Feature;
import fr.umlv.retro.features.Lambdas;
import fr.umlv.retro.features.Nestmates;
import fr.umlv.retro.features.Record;
import fr.umlv.retro.features.TryWithRessources;
import fr.umlv.retro.observer.ObserverVisitor;

public class OptFeatures implements Option {
	private final HashMap<String,Integer> map;
	private final HashMap<String, Class<? extends Feature>> map2;
	
	/**
	 * @param args
	 */
	private OptFeatures(List<String> args) {
		this.map = applyArgs(args);
		this.map2 = initMapFeaturesClass();
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
	
	private static HashMap<String, Class< ? extends Feature>> initMapFeaturesClass(){
		HashMap<String, Class<? extends Feature>>map = new HashMap<String, Class<? extends Feature>>();
		map.put("TryWithRessources", TryWithRessources.class);
		map.put("Lambda",Lambdas.class);
		map.put("Concatenation",Concat.class);
		map.put("Nestmates",Nestmates.class);
		map.put("Record",Record.class);
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

	/**
	 * Method which execute the option -Features
	 */
	@Override
	public void execute(ObserverVisitor ov, List<Class<? extends Feature>> features) {
		Objects.requireNonNull(features);
		Objects.requireNonNull(ov);
		map.forEach((k,v)->System.out.println("Feature " + k + " || Activated : " + v));
	}
	
	/**
	 * Method of creation of an OptFeatures
	 * @param args
	 * @return an option instance of OptFeatures
	 */
	public static Option create(List<String> args) {
		Option res = new OptFeatures(args);
		return res;
	}
	
	/**
	 * Method of creation of an OptFeatures
	 * @param args
	 * @return an OptFeatures
	 */
	public static OptFeatures createOptFeatures(List<String> args) {
		OptFeatures res = new OptFeatures(args);
		return res;
	}
	

	/**
	 * 
	 * @param key
	 * @return the value of the map
	 */
	public List< Class<? extends Feature>> getListFeatures() {
		List<Class<? extends Feature>> res = new ArrayList<Class<? extends Feature>>();
		for (String key :map.keySet()) {
			if( map.get(key) == 1) {
				res.add(map2.get(key));
			}
		}
		return res;
	}

}
