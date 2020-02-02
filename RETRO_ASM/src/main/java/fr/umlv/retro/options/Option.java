package fr.umlv.retro.options;


import java.nio.file.Path;
import java.util.List;

import fr.umlv.retro.features.Feature;
import fr.umlv.retro.observer.ObserverVisitor;


/**
 * Interface which represents an Option
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public interface Option {
	
	/**
	 * Method shared by each option which apply it
	 * @param ov the ObserverVisitor
	 * @param features the list of features.
	 * @param path the given path.
	 */
	/*public void execute(ObserverVisitor ov, List<Class<? extends Feature>> features,  Path path);*/

	
	/**
	 * By default, the option Target is considered inactive
	 * @return false.
	 */
	public default boolean isInfos() {
		return false;
	}

	
	/**
	 * By default, the option Target is considered inactive
	 * @return false.
	 */
	public default boolean isTarget() {
		return false;
	}


	/**
	 * By default, the option Target is considered inactive, so we consider an impossible version of target:-1
	 * @return -1.
	 */
	public default int getVersion(){
		return -1;
	}
	
	/**
	 * By default, the option Target is considered inactive, so we consider Force inactive
	 * @return -1.
	 */
	public default boolean isForce() {
		return false;
	}

	/**
	 * By default, the option Help is considered inactive
	 * @return false.
	 */
	public default boolean isHelp(){
		return false;
	}


}
