package fr.umlv.retro.options;


import java.nio.file.Path;
import java.util.List;

import fr.umlv.retro.features.Feature;
import fr.umlv.retro.observer.ObserverVisitor;

public class MainOpt {

	public static void main(String[] args, ObserverVisitor ov, Path path) {
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		List<Option> options = ParseOptions.parse(args, optfac, map);
		List<Class<? extends Feature>> features = ParseOptions.parseFeatures(args);
		options.forEach(x-> x.execute(ov, features, path));
	}

}
