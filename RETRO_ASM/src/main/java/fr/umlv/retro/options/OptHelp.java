package fr.umlv.retro.options;

import java.util.List;
import java.util.Objects;

public class OptHelp implements Option {

	private OptHelp() {
	}

	@Override
	public void execute() {
		System.out.println("Help");

	}
	
	public static Option create(List<String> args) {
		Objects.requireNonNull(args);
		return new OptHelp();
	}

}
