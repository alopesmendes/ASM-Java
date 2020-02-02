package fr.umlv.retro.options;


import java.util.List;
import java.util.Optional;

import fr.umlv.retro.features.Feature;

/**
 * Elements necessary to the Parser 
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public class OptionParsing {
	private final List<Option> options;
	private final List<Class<? extends Feature>> features;

	private OptionParsing(List<Option> options, List<Class<? extends Feature>> features) {
		this.options = options;
		this.features = features;
	}

	/**
	 * Creation of the OptionParsing from the commandline
	 * @param args array of strings
	 * @return OptionParsing
	 */
	public static OptionParsing create(String[] args){
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		List<Option> options = ParseOptions.parse(args, optfac, map);
		List<Class<? extends Feature>> features = ParseOptions.parseFeatures(args);
		return new OptionParsing(options, features);
	}
	
	/**
	 * 
	 * @return liste of the Feature classes activated
	 */
	public List<Class<? extends Feature>> getFeatures() {
		return features;
	}

	/**
	 * 
	 * @return list of options activated
	 */
	public List<Option> getOptions() {
		return options;
	}
	
	/**
	 * @return empty if target not activated otherwise a value between 5 and 13
	 */
	public Optional<Integer> version(){
		for (Option option: options) {
			if (option.isTarget()) {
				return Optional.of(option.getVersion());
			}
		}
		return Optional.empty();
	}

	/**
	 * @return the activation of -Infos
	 */
	public boolean isInfos(){
		for (Option option: options) {
			if (option.isInfos()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return false if target not activated otherwise if suboption force is activated
	 */
	public boolean isForce() {
		for (Option option: options) {
			if (option.isTarget()) {
				return option.isForce();
			}
		}
		return false;
	}
	
	public void help() {
		for (Option option: options) {
			if (option.isHelp()) {
				System.out.println("Option -Help:\n Fonctionnement du programme:\n Programme de rétrocompilation Java\n \n"
						+ "Option -Target: option qui nécessite un entier entre 5 et 13 de version pour recompiler le fichier en paramètres "
							+ "avec --force (facultatif) qui entraine la réecriture des features(par défaut tous sinon à définir avec option -features) non disponibles dans la version de recompilage voulue\n"
						+ "Option -Help: l'option actuelle qui explique le fonctionnement du programme\n"
						+ "Option -Infos: Option qui affiche la détection des features(par défaut tous sinon à définir avec option -features) dans le fichier indiqué"
						+ "Option -Features qui prend en paramètres les features que vous souhaitez réecrire avec Target ou afficher avec -Infos "
						+ "à lister en séparant avec des virgules. Features possibles(respectez orthographe): TryWithRessources, Record, Concatenation, Lambda et Nesmates\n"
						+ "prend un .class, .jar ou un répertoire en paramètre");
			}
		}
	}







}

