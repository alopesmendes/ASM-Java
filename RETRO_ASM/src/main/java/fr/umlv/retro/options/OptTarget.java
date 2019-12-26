package fr.umlv.retro.options;


import java.util.List;
import java.util.Objects;

public class OptTarget implements Option {
	private final int version;
	private final boolean force;
	
	/**
	 * @param version
	 */
	private OptTarget(List<String> args) {
		Objects.requireNonNull(args);
		if (args.get(0).equals("--force")) {
			this.force = true;
			this.version = checkValidVersion(args.get(1));
		}else {
			this.force = false;
			this.version = checkValidVersion(args.get(0));
		}
		
	}
	

	@Override
	public void execute() {
		System.out.println("Option Target avec Version: "+ version +" et Force: "+ force);

	}


	public static Option create(List<String> args) {
		Option res = new OptTarget(args);
		return res;
	}
	
	private static int checkValidVersion(String version) {
		int test = Integer.parseInt(version);
		if (test < 5 || test >13) {
			throw new IllegalArgumentException("Target Version must be between 5 and 13");
		}
		return test;
	}

}
