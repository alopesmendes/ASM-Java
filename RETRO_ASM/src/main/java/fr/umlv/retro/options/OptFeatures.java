package fr.umlv.retro.options;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OptFeatures implements Option {
	private final List<String> args;
	
	/**
	 * @param args
	 */
	public OptFeatures(int indice, List<String> args) {
		var new_args = getArgs(indice, args);
		this.args = new_args;
	}
	
	private static List<String> getArgs(int indice, List<String> args) {
		Objects.requireNonNull(args);
		int last = indice+1;
		while (last< (args.size()-1) && args.get(last).endsWith(",")) {
			last++;
		}
		last++;
		return args.subList(indice+1, last).stream().filter(x -> !(x.startsWith("-"))).collect(Collectors.toList());
		
	}

	@Override
	public void execute() {
		for (String string : args) {
			System.out.println(string);
		}
	}
	
	public static Option create(List<String> args) {
		int indice = Integer.parseInt(args.remove(0));
		Option res = new OptFeatures(indice, args);
		return res;
	}

}
