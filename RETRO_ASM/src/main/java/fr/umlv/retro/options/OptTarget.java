package fr.umlv.retro.options;


import java.util.List;
import java.util.Objects;

import org.objectweb.asm.Opcodes;


/**
 * Represents the option -Target
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public class OptTarget implements Option {
	private final int version;
	private final boolean force;
	
	/**
	 * @param args
	 * @return an OptTarget
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
	


	/**
	 * @return if Target is activated
	 */
	@Override
	public boolean isTarget() {
		return true;
	}

	/**
	 * @return the version to recompile
	 */
	@Override
	public int getVersion() {
		return Opcodes.V1_5 + (version-5);
	}

	/**
	 * @return if force is activated
	 */
	@Override
	public boolean isForce() {
		return force;
	}

	/**
	 * Method of creation of an OptTarget
	 * @param args list of Strings.
	 * @return an option instance of OptTarget
	 */
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
