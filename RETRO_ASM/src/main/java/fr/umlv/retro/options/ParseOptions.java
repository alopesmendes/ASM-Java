package fr.umlv.retro.options;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParseOptions {
	
	public static List<Option> parse(String[] args,OptionFactory optfac, OptionMap map){
		List<Option> liste = new ArrayList<Option>();
		for (int i = 0; i < args.length; i++) {
			if (isOption(args[i], map)) {
				liste.add(optfac.create(args[i],parseDescription(i, args), map));
			}
		}
		return liste;
	}
	
	
	private static boolean isOption(String type, OptionMap map) {
		return map.contains(type);
		
	}
	
	private static List<String> parseDescription(int indice,String[] args){
		List<String> liste = new ArrayList<String>();
		liste.add(Integer.toString(indice));
		liste.addAll(indice+1, Arrays.asList(args));
		return liste;
		
	}
}
