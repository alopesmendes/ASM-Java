package fr.umlv.retro.options;


import java.util.Objects;

public class OptTarget implements Option {
	private final int version;
	private final boolean force;
	
	/**
	 * @param version
	 */
	public OptTarget(int indice, String[] args) {
		Objects.requireNonNull(args);
		if (args[indice+2].equals("--force")) {
			this.force = true;
		}else {
			this.force = false;
		}
		this.version = Integer.parseInt(args[indice+1]);
	}
	

	@Override
	public void execute() {
		System.out.println("Option Target avec Version: "+ version +" et Force: "+ force);

	}

}
