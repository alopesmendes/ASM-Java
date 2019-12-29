package fr.umlv.retro.main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import fr.umlv.retro.features.FeatureVisitor;
import fr.umlv.retro.observer.ObserverVisitor;
import fr.umlv.retro.parser.Parser;

public class App {
    public static void main( String[] args ) throws IOException {
    	Path p = Paths.get("../Yo/src/concat/MyLambda.class");
    	ClassWriter cw = new ClassWriter(0);
    	ObserverVisitor ov = new ObserverVisitor(cw);
    	//Parser.parserRead(p, ov);
    	FeatureVisitor fv= new FeatureVisitor(Opcodes.V14, ov);
    	
    	Parser.parserRead(p, fv);
    	System.out.println(ov.toString());
    	Parser.change(p, cw.toByteArray());
    }
}
