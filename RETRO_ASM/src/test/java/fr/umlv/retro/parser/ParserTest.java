package fr.umlv.retro.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassWriter;

import fr.umlv.retro.observer.ObserverVisitor;


public class ParserTest {
	
	@Test @Tag("parameter")
	public void testParserWithNullPath() {
		ObserverVisitor visitor = new ObserverVisitor(new ClassWriter(0));
		assertThrows(NullPointerException.class, () -> Parser.parserRead(null, visitor));
	}
	
	@Test  @Tag("parameter")
	public void testParserWithNullVisitor() {
		
		assertThrows(NullPointerException.class, () -> Parser.parserRead(Paths.get("../yo.jar"), null));
	}
	
	@Test @Tag("parameter")
	public void testInexistantPath() {
		ObserverVisitor visitor = new ObserverVisitor(new ClassWriter(0));
		assertAll(
			() -> assertThrows(IOException.class, () -> Parser.parserRead(Paths.get(" "), visitor)),
			() -> assertThrows(IOException.class, () -> Parser.parserRead(Paths.get("Inexistant.class"), visitor)),
			() -> assertThrows(IOException.class, () -> Parser.parserRead(Paths.get("../Yo/src/Inexistant"), visitor)),
			() -> assertThrows(IOException.class, () -> Parser.parserRead(Paths.get("../Inexistant.jar"), visitor))
		);
	}	
	
}
