package fr.umlv.retro.parser;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;

public class Parser {
	@FunctionalInterface
	private static interface IOUtils {
		void execute(Path path, NoName noName, ParsingOptions... options);
	}
	
	/**
	 * With a given path determines if is .class.
	 * @param path
	 * @return true if is .class
	 */
	static boolean isClassFile(Path path) {
		return !Files.isDirectory(path) && path.toString().endsWith(".class");
	}
	
	/**
	 * With a given zipEntry if is .class.
	 * @param zipEntry
	 * @return true if is .class
	 */
	static boolean isClassFile(ZipEntry zipEntry) {
		return !zipEntry.isDirectory() && zipEntry.getName().endsWith(".class");
	}
	
	private static IOUtils parsingFile() {
		return (path, noName, options) -> { 
			try (InputStream in = Files.newInputStream(path)) {
				noName.stock(path.toString(), new ClassReader(in));
			} catch (IOException e) { throw new IOError(e); }
		};
	}
	
	private static void parsingEntry(Path path, ZipInputStream zStream, NoName noName, ParsingOptions...options) throws IOException {
		for (ZipEntry entry = zStream.getNextEntry(); entry != null; entry = zStream.getNextEntry()) {
			if (!isClassFile(entry)) { continue; }
			noName.stock(entry.toString(), new ClassReader(zStream));
		}
	}
	
	private static IOUtils parsingJar() {
		return (path, noName, options) -> {
			try(InputStream in = Files.newInputStream(path);
				ZipInputStream zStream = new ZipInputStream(in)) {
				parsingEntry(path, zStream, noName, options);
			} catch (IOException e) { throw new IOError(e); }
		};
	}
	
	private static void parsingDirectory(Path dir, NoName noName, ParsingOptions...options) throws IOException {
		IOUtils ioUtils = parsingFile();
		try(Stream<Path> paths = Files.list(dir)) {
			paths.filter(p -> Parser.isClassFile(p) || p.toString().endsWith(".jar")).forEach(path -> ioUtils.execute(path, noName, options));
		}
	}
	
	private static void chooseParser(Path path, NoName noName, ParsingOptions...options) throws IOException {
		if (!Files.isDirectory(path)) {
			if (!isClassFile(path)) {
				parsingJar().execute(path, noName, options);
			} else { parsingFile().execute(path, noName, options); }
		} else { parsingDirectory(path, noName, options); }
	}
	
	private static void requires(Path path, NoName noName, ParsingOptions...options) {
		Objects.requireNonNull(path);
		Objects.requireNonNull(noName);
		Objects.requireNonNull(List.of(options));
		if (!Files.isDirectory(path) && !isClassFile(path) && !path.toString().endsWith(".jar")) {
			throw new IllegalArgumentException("path as to be a .class or a directory or a jar");
		}
	}
		
	public static void parse(Path path, NoName noName, ParsingOptions...options) throws IOException {
		requires(path, noName, options);
		chooseParser(path, noName, options);
		noName.execute(path, options);
	}
}
