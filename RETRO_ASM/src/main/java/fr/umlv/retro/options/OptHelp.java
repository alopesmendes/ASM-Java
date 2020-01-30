package fr.umlv.retro.options;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import fr.umlv.retro.features.Feature;
import fr.umlv.retro.observer.ObserverVisitor;

public class OptHelp implements Option {

	private OptHelp() {
	}

	/**
	 * Method which execute the option -Help
	 */
	@Override
	public void execute(ObserverVisitor ov, List<Class<? extends Feature>> features,  Path path) {
		System.out.println("Option -Help:\n Fonctionnement du programme:\n Programme de rétrocompilation Java\n \n"
				+ "Option -Target: option qui nécessite un entier entre 5 et 13 de version pour recompiler le fichier en paramètres "
					+ "avec --force (facultatif) qui entraine la réecriture des features(par défaut tous sinon à définir avec option -features) non disponibles dans la version de recompilage voulue\n"
				+ "Option -Help: l'option actuelle qui explique le fonctionnement du programme\n"
				+ "Option -Infos: Option qui affiche la détection des features(par défaut tous sinon à définir avec option -features) dans le fichier indiqué"
				+ "Option -Features qui prend en paramètres les features que vous souhaitez réecrire avec Target ou afficher avec -Infos "
				+ "à lister en séparant avec des virgules. Features possibles(respectez orthographe): TryWithRessources, Record, Concatenation, Lambda et Nesmates\n"
				+ "prend un .class, .jar ou un répertoire en paramètre");
	}

	@Override
	public boolean isTarget() {
		return true;
	}

	/**
	 * Method of creation of an OptHelp
	 * @param args
	 * @return an option instance of OptHelp
	 */
	public static Option create(List<String> args) {
		Objects.requireNonNull(args);
		return new OptHelp();
	}

}
