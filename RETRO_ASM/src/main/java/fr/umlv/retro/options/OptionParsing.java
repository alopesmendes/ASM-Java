package fr.umlv.retro.options;


import java.util.List;
import java.util.Optional;

import fr.umlv.retro.features.Feature;

public class OptionParsing {
	private final List<Option> options;
	private final List<Class<? extends Feature>> features;

	private OptionParsing(List<Option> options, List<Class<? extends Feature>> features) {
		this.options = options;
		this.features = features;
	}

	/*

	 */
	public static OptionParsing create(String[] args){
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		List<Option> options = ParseOptions.parse(args, optfac, map);
		List<Class<? extends Feature>> features = ParseOptions.parseFeatures(args);
		return new OptionParsing(options, features);
	}
	/*

	 */
	public List<Class<? extends Feature>> getFeatures() {
		return features;
	}

	public List<Option> getOptions() {
		return options;
	}
	/*

	 */
	public Optional<Integer> version(){
		for (Option option: options) {
			if (option.isTarget()) {
				return Optional.of(option.getVersion());
			}
		}
		return Optional.empty();
	}

	/*

	 */
	public boolean isInfos(){
		for (Option option: options) {
			if (option.isInfos()) {
				return true;
			}
		}
		return false;
	}



	
	public static void main(String[] args) {
		var options = create(args);
		System.out.println(options.version().get());
	}



}
