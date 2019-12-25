package fr.umlv.retro.options;


import java.util.List;

public class MainOpt {

	public static void main(String[] args) {
		OptionMap map = OptionMap.initMap();
		OptionFactory optfac = new OptionFactory();
		List<Option> options = ParseOptions.parse(args, optfac, map);
		options.forEach(x-> x.execute());
	}

}
