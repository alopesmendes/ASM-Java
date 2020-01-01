package fr.umlv.retro.parser;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import fr.umlv.retro.features.FeatureVisitor;
import fr.umlv.retro.observer.ObserverVisitor;

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
	
	/**
	 * Creates the ClassReader according to a class file.
	 * @param path
	 * @return ClassReader corresponding to file.
	 * @throws IOException
	 */
	private static ClassReader parserFile(Path path) throws IOException {
		return new ClassReader(parsingPathToByte(path));		
	}
	

	/**
	 * Creates a List of ClassReader each ClassReader relied to a class file.
	 * @param path
	 * @return List of ClassReader according to a Directory.
	 * @throws IOException
	 */
	private static List<ClassReader> parserDirectory(Path path) throws IOException {
		try(Stream<Path> list = Files.list(path)) {
			return list.filter(Parser::isClassFile).map(t -> {
				try { return parserFile(t); } 
				catch (IOException e) { throw new IOError(e); }
			}).collect(Collectors.toUnmodifiableList());
		}
	}
	
	

	/**
	 * Creates a List of ClassReader each ClassReader relied to a class file.
	 * @param path
	 * @return List of ClassReader according to a jar.
	 * @throws IOException
	 */
	private static List<ClassReader> parserJar(Path path) throws IOException {
		ArrayList<ClassReader> list = new ArrayList<>();
		try (InputStream in = Files.newInputStream(path); 
			ZipInputStream zip = new ZipInputStream(in)) {
			
			for (ZipEntry jar = zip.getNextEntry(); jar != null; jar = zip.getNextEntry()) {
				if (!(isClassFile(jar))) { continue; }
				list.add(new ClassReader(zip));
			}
			return Collections.unmodifiableList(list);
		}	
	}
	
	/**
	 * Chooses how to parse our path and return the List of ClassReader.
	 * @param path
	 * @return List of ClassReader.
	 * @throws IOException
	 */
	private static List<ClassReader> chooseParser(Path path) throws IOException {
		if (!Files.isDirectory(path)) {
			if (path.toString().endsWith(".jar")) { return parserJar(path); }
			else { return List.of(parserFile(path)); }
		} 
		else { return parserDirectory(path); }
	}
	
	/**
	 * Parses a given path and only reads.
	 * @param path
	 * @param visitor
	 * @throws IOException
	 */
	public static void parserRead(Path path, ClassVisitor visitor) throws IOException {
		Objects.requireNonNull(path);
		Objects.requireNonNull(visitor);
		
		List<ClassReader> readers = chooseParser(path);
		readers.forEach(r -> r.accept(visitor, ClassReader.EXPAND_FRAMES));
	}
	
	public static void parserWriter(int version, Path path, ObserverVisitor visitor) throws IOException {
		Objects.requireNonNull(path);
		Objects.requireNonNull(visitor);
		
		List<ClassReader> readers = chooseParser(path);
		
		readers.forEach(r -> {
			ClassWriter cw = new ClassWriter(r, ClassWriter.COMPUTE_MAXS);
			FeatureVisitor fv = new FeatureVisitor(version, cw, visitor);
			r.accept(fv, ClassReader.EXPAND_FRAMES);
			try {
				change(path, cw.toByteArray());
			} catch (IOException e) { throw new IOError(e); }
		});
		
	}
	
	public static void change(Path path, byte[] bytes) throws IOException {
		Objects.requireNonNull(path);
		try(OutputStream out = Files.newOutputStream(path)) {
			out.write(bytes);
		}
	}

}
