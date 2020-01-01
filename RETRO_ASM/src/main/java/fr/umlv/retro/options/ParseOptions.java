package fr.umlv.retro.options;

import java.util.ArrayList;
import java.util.List;

import fr.umlv.retro.features.Feature;

public class ParseOptions {
	
	/**
	 * Parse the command line and returns a list of Options
	 * @param args
	 * @param optfac
	 * @param map
	 * @return a list of Option
	 */
	public static List<Option> parse(String[] args,OptionFactory optfac, OptionMap map){
		List<Option> liste = new ArrayList<Option>();
		for (int i = 0; i < args.length; i++) {
			if (isOption(args[i], map)) {
				liste.add(optfac.create(args[i],parseDescription(i, args), map));
			}
		}
		return liste;
	}
	
	
	public static List<Class<? extends Feature>> parseFeatures(String[] args){
		List<Class<? extends Feature>> liste = new ArrayList<Class<? extends Feature>>();
		for (int i = 0; i < args.length; i++) {
			if (isOptionFeature(args[i])) {
				liste = OptFeatures.createOptFeatures(parseDescription(i, args)).getListFeatures();
			}
		}
		return liste;
	}
	

	
	private static boolean isOption(String type, OptionMap map) {
		return map.contains(type);	
	}
	
	private static boolean isOptionFeature(String type){
		return type.equals("-Features");
	}
	
	private static List<String> parseDescription(int indice,String[] args){
		List<String> liste = new ArrayList<String>();
		int test = indice+1;
		while (test<args.length) {
			liste.add(args[test]);
			test++;
		}
		return liste;		
	}
}
