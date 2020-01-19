package fr.umlv.retro.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


public class ParserTest {
	
	@Test @Tag("parameter")
	public void testParserWithNullPath() {
		assertThrows(NullPointerException.class, () -> Parser.parse(null, NoName.create()));
	}
	
	@Test  @Tag("parameter")
	public void testParserWithNullVisitor() {
		
		assertThrows(NullPointerException.class, () -> Parser.parse(Paths.get("../yo.jar"), null));
	}
	
	@Test @Tag("parameter")
	public void testInexistantPath() {
		assertAll(
			() -> assertThrows(IOException.class, () -> Parser.parse(Paths.get(" "), NoName.create())),
			() -> assertThrows(IOException.class, () -> Parser.parse(Paths.get("Inexistant.class"), NoName.create())),
			() -> assertThrows(IOException.class, () -> Parser.parse(Paths.get("../Yo/src/Inexistant"), NoName.create())),
			() -> assertThrows(IOException.class, () -> Parser.parse(Paths.get("../Inexistant.jar"), NoName.create()))
		);
	}	
	
}
