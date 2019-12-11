package RETRO_PROJECT.PARSER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

public class Parser { 
	
	/**
	 * @param path
	 * @return array of byte according to path.
	 */
	private static byte[] parsingFile(Path path) {
		try {
			return Files.readAllBytes(path);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
	}
	
	/**
	 * Parses all files .class in given path.
	 * @param path
	 * @param visitor
	 * @throws IOException
	 */
	public static void parser(Path path, ClassVisitor visitor) throws IOException {
		try(var walk = Files.walk(path)) {
			walk.filter(p -> p.toString().endsWith(".class")).
			map(Parser::parsingFile).map(ClassReader::new).
			forEach(reader -> reader.accept(visitor, ClassReader.EXPAND_FRAMES));
		}
	}	
	
	public static List<ClassReader> parseReader(Path path) throws IOException {
		try(var walk = Files.walk(path)) {
			return 	walk.filter(p -> p.toString().endsWith(".class")).
					map(Parser::parsingFile).map(ClassReader::new).
					collect(Collectors.toList());
		}
	}
	
}
