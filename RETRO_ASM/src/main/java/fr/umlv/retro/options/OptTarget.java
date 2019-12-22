package fr.umlv.retro.options;


import java.util.List;
import java.util.Objects;

public class OptTarget implements Option {
	private final int version;
	private final boolean force;
	
	/**
	 * @param version
	 */
	private OptTarget(int indice, List<String> args) {
		Objects.requireNonNull(args);
		if (args.get(indice+2).equals("--force")) {
			this.force = true;
		}else {
			this.force = false;
		}
		this.version = Integer.parseInt(args.get(indice+1));
	}
	

	@Override
	public void execute() {
		System.out.println("Option Target avec Version: "+ version +" et Force: "+ force);

	}


	public static Option create(List<String> args) {
		int indice = Integer.parseInt(args.remove(0));
		Option res = new OptTarget(indice, args);
		return res;
	}

}
