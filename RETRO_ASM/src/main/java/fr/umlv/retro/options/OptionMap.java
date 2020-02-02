package fr.umlv.retro.options;


import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class OptionMap {
	private final HashMap<String, Function<List<String>, Option>> map;

	private OptionMap() {
		this.map = new HashMap<String, Function<List<String>,Option>>();
	}
	
	
	/**
	 * 
	 * @param key a String.
	 * @return a function of creation of an option corresponding to a key
	 */
	public Function<List<String>,Option> get(String key){
		Objects.requireNonNull(key);
		return map.get(key);
	}
	
	private void put(String key, Function<List<String>,Option> value) {
		map.put(key, value);
	}
	
	/**
	 * 
	 * @param key a String.
	 * @return true if key is a key of the map
	 */
	public boolean contains(String key) {
		Objects.requireNonNull(key);
		return map.containsKey(key);
	}
	
	/**
	 * method of creation of the OptionMap
	 * @return the OptionMap which is used by the program
	 */
	public static OptionMap initMap() {
		OptionMap map = new OptionMap();
		map.put("-Target", OptTarget::create);
		map.put("-Features", OptFeatures::create);
		map.put("-Help", OptHelp::create);
		map.put("-Infos", OptInfo::create);
		return map;
	}
}
