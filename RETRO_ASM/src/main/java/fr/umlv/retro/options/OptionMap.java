package fr.umlv.retro.options;


import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class OptionMap {
	private final HashMap<String, Function<List<String>, Option>> map;

	public OptionMap(HashMap<String, Function<List<String>, Option>> map) {
		this.map = map;
	}
	
	public Function<List<String>,Option> get(String key){
		Objects.requireNonNull(key);
		return map.get(key);
	}
	
	public void put(String key, Function<List<String>,Option> value) {
		map.put(key, value);
	}
	
	public boolean contains(String key) {
		return map.containsKey(key);
	}
	
}
