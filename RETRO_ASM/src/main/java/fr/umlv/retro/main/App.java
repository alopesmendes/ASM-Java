package fr.umlv.retro.main;

import java.io.IOException;
import java.nio.file.Paths;

import org.objectweb.asm.Opcodes;

import fr.umlv.retro.observer.ObserverVisitor;
import fr.umlv.retro.parser.Parser;

public class App {
    public static void main( String[] args ) throws IOException {
    	ObserverVisitor visitor = new ObserverVisitor(Opcodes.ASM7);
    	Parser.parserRead(Paths.get("../Yo/src/concat/lambda"), visitor);
    	System.out.println(visitor);
    }
}
