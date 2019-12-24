package fr.umlv.retro.main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import fr.umlv.retro.features.Concat;
import fr.umlv.retro.features.Feature;
import fr.umlv.retro.features.FeaturesHistory;
import fr.umlv.retro.features.Lambdas;
import fr.umlv.retro.features.Nestmates;
import fr.umlv.retro.features.TryWithRessources;
import fr.umlv.retro.observer.ObserverHistory;
import fr.umlv.retro.observer.ObserverVisitor;
import fr.umlv.retro.parser.Parser;

public class App {
    public static void main( String[] args ) throws IOException {
    	ObserverVisitor visitor = new ObserverVisitor(Opcodes.ASM7);
    	Parser.parserRead(Paths.get("../Yo/src/concat/lambda"), visitor);
    	System.out.println(visitor.displayFeatureHistory(Lambdas.class));
    }
}
