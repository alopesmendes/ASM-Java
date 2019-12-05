package fr.umlv.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

public class Parser { 
	
	public static void parser(Path path, ClassVisitor visitor) throws IOException {
		try(var walk = Files.walk(path)) {
			walk.filter(p -> p.toString().endsWith(".class")).
			map(Parser::parsingFile).map(ClassReader::new).
			forEach(reader -> reader.accept(visitor, ClassReader.EXPAND_FRAMES));
		}
	}
	
	private static byte[] parsingFile(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}
	
	
}
