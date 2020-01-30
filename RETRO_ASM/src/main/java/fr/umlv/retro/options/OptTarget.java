package fr.umlv.retro.options;


import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import org.objectweb.asm.Opcodes;

import fr.umlv.retro.features.Feature;
import fr.umlv.retro.observer.ObserverVisitor;

public class OptTarget implements Option {
	private final int version;
	private final boolean force;
	
	/**
	 * @param args
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
	 * Method which execute the option -Target
	 * @throws IOException 
	 */
	@Override
	public void execute(ObserverVisitor ov, List<Class<? extends Feature>> features, Path path) {
		/*Objects.requireNonNull(ov);
		Objects.requireNonNull(features);
		System.out.println("Option Target avec Version: "+ version +" et Force: "+ force+"\nRéecriture pas encore correctement implémenté car ajout réécriture des features en cours");
		if (!path.toString().endsWith(".class") || !(features.contains(Concat.class) && features.size()==1)) {
			System.out.println("Actuellement, ne fonctionne qu'avec des .class avec la feature concatenation");
		}else{try {
			Parser.parse(path, PathOperation.create());
		} catch (IOException e) {
			e.printStackTrace();
		}}*/
	}

	@Override
	public boolean isTarget() {
		return true;
	}

	@Override
	public int getVersion() {
		return Opcodes.V1_5 + (version-5);
	}

	public boolean isForce() {
		return force;
	}

	/**
	 * Method of creation of an OptTarget
	 * @param args
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
