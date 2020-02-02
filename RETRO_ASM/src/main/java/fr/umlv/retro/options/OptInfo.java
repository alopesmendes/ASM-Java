package fr.umlv.retro.options;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import fr.umlv.retro.features.Feature;
import fr.umlv.retro.observer.ObserverVisitor;


/**
 * Represents the option -Infos
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public class OptInfo implements Option {

	private OptInfo() {
	}
	
	/**
	 * Method of creation of an OptInfo
	 * @param args list of args.
	 * @return an option instance of OptInfo
	 */
	public static Option create(List<String> args) {
		Objects.requireNonNull(args);
		return new OptInfo();
	}

	/**
	 * 
	 * @return if the option -Infos is activated 
	 */
	@Override
	public boolean isInfos() {
		return true;
	}
}
