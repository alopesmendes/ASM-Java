package fr.umlv.retro.options;

import java.util.List;
import java.util.Objects;

public class OptHelp implements Option {

	private OptHelp() {
	}

	/**
	 * Method which excecute the option -Help
	 */
	@Override
	public void execute() {
		System.out.println("Help");

	}
	
	/**
	 * Method of creation of an OptHelp
	 * @param args
	 * @return an option instance of OptHelp
	 */
	public static Option create(List<String> args) {
		Objects.requireNonNull(args);
		return new OptHelp();
	}

}
