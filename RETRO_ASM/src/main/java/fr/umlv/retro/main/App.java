package fr.umlv.retro.main;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import fr.umlv.retro.observer.ObserverVisitor;
import fr.umlv.retro.options.MainOpt;
import fr.umlv.retro.parser.Parser;

public class App {
	private static Path getPathOf(String[] args) {
		Optional<String> s = Arrays.stream(args).filter(a -> !a.startsWith("-")).findFirst();
		if (s.isEmpty()) {
			return Path.of("");
		} else {
			return Path.of(s.get());
		}
	}
	
    public static void main( String[] args ) throws IOException {
    	Path p = getPathOf(args);
    	//ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
    	ObserverVisitor ov = new ObserverVisitor();
    	Parser.parserRead(p, ov);
    	MainOpt.main(args, ov);
    	//Parser.parserRead(p, ov);
    	//FeatureVisitor fv= new FeatureVisitor(Opcodes.V13, cw, ov);
    	
    	//Parser.parserRead(p, ov);
    	//System.out.println(ov.toString());
    	//Parser.change(p, cw.toByteArray());
    }
}
