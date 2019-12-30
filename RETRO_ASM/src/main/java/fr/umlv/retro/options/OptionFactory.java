package fr.umlv.retro.options;

import java.util.List;
import java.util.Objects;

public class OptionFactory {
	
	
	/**
	 * Method factory of an option
	 * @param type
	 * @param description
	 * @param map
	 * @return an option
	 */
	public Option create(String type, List<String> description, OptionMap map) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(description);
		Objects.requireNonNull(map);
		return map.get(type).apply(description);
	}
}
