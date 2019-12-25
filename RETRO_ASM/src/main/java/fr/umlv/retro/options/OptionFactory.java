package fr.umlv.retro.options;

import java.util.List;

public class OptionFactory {
	
	public Option create(String type, List<String> description, OptionMap map) {
		return map.get(type).apply(description);
	}
}
