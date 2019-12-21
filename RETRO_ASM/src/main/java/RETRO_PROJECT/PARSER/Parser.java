package RETRO_PROJECT.PARSER;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

public class Parser { 
	
	/**
	 * Parser a path into a byte array. 
	 * @param path
	 * @return array of byte according to path.
	 * @throws IOException 
	 */
	private static byte[] parsingPathToByte(Path path) throws IOException {
		return Files.readAllBytes(path);
		
	}
	
	/**
	 * With a given path determines if is .class.
	 * @param path
	 * @return true if is .class
	 */
	private static boolean isClassFile(Path path) {
		return !Files.isDirectory(path) && path.toString().endsWith(".class");
	}
	
	/**
	 * With a given zipEntry if is .class.
	 * @param zipEntry
	 * @return true if is .class
	 */
	private static boolean isClassFile(ZipEntry zipEntry) {
		return !zipEntry.isDirectory() && zipEntry.getName().endsWith(".class");
	}
	
	private static void parserFile(Path path, ClassVisitor visitor) throws IOException {
		ClassReader reader = new ClassReader(parsingPathToByte(path));
		reader.accept(visitor, ClassReader.EXPAND_FRAMES);
		
	}
	
	/**
	 * Parses all files .class in given path corresponding directory.
	 * @param path
	 * @param visitor
	 * @throws IOException
	 */
	private static void parserDirectory(Path path, ClassVisitor visitor) throws IOException {
		try(Stream<Path> list = Files.list(path)) {
			list.filter(Parser::isClassFile).forEach(p -> {
				try { parserFile(p, visitor); }
				catch (IOException e) { throw new IOError(e); }
			});
		}
	}
	
	
	/**
	 * Parses all files .class in given path corresponding to a jar.
	 * @param path
	 * @param visitor
	 * @throws IOException
	 */
	private static void parserJar(Path path, ClassVisitor visitor) throws IOException {
		try (InputStream in = Files.newInputStream(path)) {
			ZipInputStream zip = new ZipInputStream(in);
			for (ZipEntry jar = zip.getNextEntry(); jar != null; jar = zip.getNextEntry()) {
				if (!(isClassFile(jar))) { continue; }
				new ClassReader(zip).accept(visitor, ClassReader.EXPAND_FRAMES);
			}
		}	
	}	
	
	public static void parser(Path path, ClassVisitor visitor) throws IOException {
		Objects.requireNonNull(path);
		Objects.requireNonNull(visitor);
		if (!Files.isDirectory(path)) {
			if (path.toString().endsWith(".jar")) { parserJar(path, visitor); }
			else { parserFile(path, visitor); }
		} 
		else { parserDirectory(path, visitor); }
	}

}
