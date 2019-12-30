package fr.umlv.retro.options;


import java.util.List;

import fr.umlv.retro.features.Feature;

public class MainOpt {

	public static void main(String[] args) {
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		List<Option> options = ParseOptions.parse(args, optfac, map);
		OptFeatures optfeatures = ParseOptions.getFeatures(options);
		List<Class<? extends Feature>> features = optfeatures.getListFeatures();
	}

}
