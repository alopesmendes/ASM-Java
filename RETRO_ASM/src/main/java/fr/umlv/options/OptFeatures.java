package fr.umlv.options;


import java.util.Arrays;
import java.util.Objects;

public class OptFeatures implements Option {
	private final String[] args;
	
	/**
	 * @param args
	 */
	public OptFeatures(int indice, String[] args) {
		var new_args = getArgs(indice, args);
		this.args = new_args;
	}
	
	private static String[] getArgs(int indice, String[] args) {
		Objects.requireNonNull(args);
		int last = indice+1;
		while (last< (args.length-1) && args[last].endsWith(",")) {
			last++;
		}
		last++;
		var test = Arrays.copyOfRange(args, indice+1, last);
		String[] args_tests= Arrays.stream(test).filter(x -> !(x.startsWith("-"))).toArray(String[]::new);
		return args_tests;
		
	}

	@Override
	public void execute() {
		for (String string : args) {
			System.out.println(string);
		}
	}

}
