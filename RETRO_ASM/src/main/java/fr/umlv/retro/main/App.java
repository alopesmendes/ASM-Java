package fr.umlv.retro.main;

import java.io.IOException;
import java.nio.file.Paths;

import fr.umlv.retro.observer.ObserverVisitor;
import fr.umlv.retro.parser.Parser;

public class App {
    public static void main( String[] args ) throws IOException {
    	//ClassWriter cw = new ClassWriter(0);;
    	ObserverVisitor visitor = new ObserverVisitor();
    	Parser.parserRead(Paths.get("../Yo/src/concat/MyRecord.class"), visitor);
    	System.out.println(visitor);
    	//Parser.change(Paths.get("../Yo/src/concat/lambda/MyConcat.class"), cw.toByteArray());
    }
}
