package fr.umlv.retro.options;

import java.util.List;
import java.util.Objects;

/**
 * Factory of the options
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public class OptionFactory {
	
	
	/**
	 * Method factory of an option
	 * @param type of option
	 * @param description list of String.
	 * @param map a OptionMap.
	 * @return an option
	 */
	public Option create(String type, List<String> description, OptionMap map) {
		Objects.requireNonNull(type);
		Objects.requireNonNull(description);
		Objects.requireNonNull(map);
		return map.get(type).apply(description);
	}
	
}
