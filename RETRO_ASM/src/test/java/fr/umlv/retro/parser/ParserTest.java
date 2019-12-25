package fr.umlv.retro.parser;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;


public class ParserTest {
	
	@Test @Tag("parameter")
	public void testParserWithNullPath() {
		ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) { };
		assertThrows(NullPointerException.class, () -> Parser.parserRead(null, visitor));
	}
	
	@Test  @Tag("parameter")
	public void testParserWithNullVisitor() {
		
		assertThrows(NullPointerException.class, () -> Parser.parserRead(Paths.get("../yo.jar"), null));
	}
	
	@Test @Tag("parameter")
	public void testInexistantPath() {
		ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) { };
		assertAll(
			() -> assertThrows(IOException.class, () -> Parser.parserRead(Paths.get(" "), visitor)),
			() -> assertThrows(IOException.class, () -> Parser.parserRead(Paths.get("Inexistant.class"), visitor)),
			() -> assertThrows(IOException.class, () -> Parser.parserRead(Paths.get("../Yo/src/Inexistant"), visitor)),
			() -> assertThrows(IOException.class, () -> Parser.parserRead(Paths.get("../Inexistant.jar"), visitor))
		);
	}

	
	@Test @Tag("FileClass")
	public void testParseFileClass() {
	
	}
	
	@Test @Tag("DirectoryWithoutClass")
	public void testParseDirectoryWithoutClass() {
		
	}
	
	@Test @Tag("DirectoryClass")
	public void testParseDirectoryClass() {
		
	}
	
	@Test @Tag("JarClass")
	public void testParseJarClass() {
		
	}
	
	
	
	
	
	
}
