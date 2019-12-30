package fr.umlv.retro.options;

import java.util.List;
import java.util.Objects;


import fr.umlv.retro.features.Feature;
import fr.umlv.retro.observer.ObserverVisitor;

public class OptInfo implements Option {

	private OptInfo() {
	}
	/**
	 * Method which execute the option -Infos
	 */
	@Override
	public void execute(ObserverVisitor ov, List<Class<? extends Feature>> features) {
		System.out.println("Voici les infos");
    	if (features.size()==0) {
    		System.out.println(ov.toString());
    	}else {
    		for (Class<? extends Feature> feature : features) {
				ov.displayFeatureHistory(feature);
			}
    	}
	}
	
	/**
	 * Method of creation of an OptInfo
	 * @param args
	 * @return an option instance of OptInfo
	 */
	public static Option create(List<String> args) {
		Objects.requireNonNull(args);
		return new OptInfo();
	}
	
	/**
	 * determine the features
	 */
	@Override
	public boolean isFeatures() {
		return false;
	}

}
