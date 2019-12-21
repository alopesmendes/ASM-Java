package RETRO_PROJECT.PARSER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

public class Parser { 
	
	/**
	 * 
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
	 * Parses all files .class in given path corresponding to a project.
	 * @param path
	 * @param visitor
	 * @throws IOException
	 */
	public static void parserFile(Path path, ClassVisitor visitor) throws IOException {
		try(Stream<Path> walk = Files.walk(path)) {
			walk.filter(p -> p.toString().endsWith(".class")).
			map(Parser::parsingFile).map(ClassReader::new).
			forEach(reader -> reader.accept(visitor, ClassReader.EXPAND_FRAMES));
		}
	}
	
	
	/**
	 * Parses all files .class in given path corresponding to a jar.
	 * @param path
	 * @param visitor
	 * @throws IOException
	 */
	public static void parserJar(Path path, ClassVisitor visitor) throws IOException {
		try (ZipInputStream in = new ZipInputStream(Files.newInputStream(path))) {
			for (ZipEntry jar = in.getNextEntry(); jar != null; jar = in.getNextEntry()) {
				if (!(jar.getName().endsWith(".class"))) {
					continue;
				}
				new ClassReader(in).accept(visitor, ClassReader.EXPAND_FRAMES);;
			}
		}
		
	}	

}
