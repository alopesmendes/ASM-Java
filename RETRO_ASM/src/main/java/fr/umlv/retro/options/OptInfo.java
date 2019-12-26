package fr.umlv.retro.options;

import java.util.List;
import java.util.Objects;

public class OptInfo implements Option {

	private OptInfo() {
	}
	
	@Override
	public void execute() {
		System.out.println("Voici les infos");

	}
	
	public static Option create(List<String> args) {
		Objects.requireNonNull(args);
		return new OptInfo();
	}

}
