package fr.umlv.retro.options;


import java.nio.file.Path;
import java.util.List;

import fr.umlv.retro.features.Feature;
import fr.umlv.retro.observer.ObserverVisitor;

public interface Option {
	
	/**
	 * Method shared by each option which apply it
	 * @param ov the ObserverVisitor
	 * @param features the list of features.
	 * @param path the given path.
	 */
	public void execute(ObserverVisitor ov, List<Class<? extends Feature>> features,  Path path);

	
	/**
	 * @return false.
	 */
	public default boolean isInfos() {
		return false;
	}

	
	/**
	 * @return false.
	 */
	public default boolean isTarget() {
		return false;
	}


	/**
	 * @return -1.
	 */
	public default int getVersion(){
		return -1;
	}
	
	/**
	 * @return false.
	 */
	public default boolean isForce() {
		return false;
	}

	/**
	 * @return false.
	 */
	public default boolean isHelp(){
		return false;
	}


}
