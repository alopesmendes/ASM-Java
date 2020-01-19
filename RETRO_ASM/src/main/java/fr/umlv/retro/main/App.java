package fr.umlv.retro.main;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import fr.umlv.retro.parser.NoName;
import fr.umlv.retro.parser.Parser;


public class App {
	
	private static boolean isNotNumber(String str) {
		return !str.matches("-?(0|[1-9]\\d*)");
	}
	
	private static Path getPathOf(String[] args) {
		List<String> features = Arrays.asList(new String[]{"TryWithRessources","Lambda","Concatenation","Nestmates","Record"});
		Optional<String> s = Arrays.stream(args).filter(a -> !a.startsWith("-")).filter(App::isNotNumber).
				filter(x -> !x.endsWith(",")).filter(x->!features.contains(x)).findFirst();
		if (s.isEmpty()) { return Path.of(""); } 
		else { return Path.of(s.get()); }
	}
	
    public static void main( String[] args ) throws IOException {
    	Path path = getPathOf(args);
    	Parser.parse(path, NoName.create());
    	//MainOpt.main(args, ov ,p);
    }
}
