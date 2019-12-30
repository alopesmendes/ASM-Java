package fr.umlv.retro.options;


import java.util.List;

import fr.umlv.retro.features.Feature;
import fr.umlv.retro.observer.ObserverVisitor;

public interface Option {
	/**
	 * Method shared by each option which apply it
	 * */
	public void execute(ObserverVisitor ov, List<Class<? extends Feature>> features);
	public boolean isFeatures();
}
