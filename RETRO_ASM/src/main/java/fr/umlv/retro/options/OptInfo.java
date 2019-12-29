package fr.umlv.retro.options;

import java.util.List;
import java.util.Objects;

public class OptInfo implements Option {

	private OptInfo() {
	}
	/**
	 * Method which excecute the option -Infos
	 */
	@Override
	public void execute() {
		System.out.println("Voici les infos");

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

}
