package fr.umlv.retro.options;

import java.util.List;
import java.util.Objects;

public class OptHelp implements Option {

	private OptHelp() {
	}

	/**
	 * @return the activation of option -Help
	 */
	@Override
	public boolean isHelp() {
		return true;
	}

	/**
	 * Method of creation of an OptHelp
	 * @param args list of args.
	 * @return an option instance of OptHelp
	 */
	public static Option create(List<String> args) {
		Objects.requireNonNull(args);
		return new OptHelp();
	}

}
