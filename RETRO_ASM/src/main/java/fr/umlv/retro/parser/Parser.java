package fr.umlv.retro.parser;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarInputStream;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

import org.objectweb.asm.ClassReader;

public class Parser {
	@FunctionalInterface
	private static interface IOUtils {
		void execute(Path path, PathOperation noName, ParsingOptions... options);
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
	
	/**
	 * Creates a IOUtils that will stock a .class.
	 * @return IOUtils to parserFile.
	 */
	private static IOUtils parsingFile() {
		return (path, pOperation, options) -> { 
			try (InputStream in = Files.newInputStream(path)) {
				pOperation.stock(path.toString(), new ClassReader(in));
			} catch (IOException e) { throw new IOError(e); }
		};
	}
	
	/**
	 * Stocks every .class of a jar and it's classReader.
	 * @param path
	 * @param jStream
	 * @param pOperation
	 * @param options
	 * @throws IOException
	 */
	private static void parsingEntry(Path path, JarInputStream jStream, PathOperation pOperation, ParsingOptions...options) throws IOException {
		for (ZipEntry entry = jStream.getNextEntry(); entry != null; entry = jStream.getNextEntry()) {
			if (!isClassFile(entry)) { continue; }
			pOperation.stock(entry.toString(), new ClassReader(jStream));
		}
	}
	
	/**
	 * Parses every Entry of a jar.
	 * @return IOUtils of jar.
	 */
	private static IOUtils parsingJar() {
		return (path, pOperation, options) -> {
			try(InputStream in = Files.newInputStream(path);
				JarInputStream jStream = new JarInputStream(in)) {
				parsingEntry(path, jStream, pOperation, options);
			} catch (IOException e) { throw new IOError(e); }
		};
	}
	
	/**
	 * Parses every .class or .jar of a given directory.
	 * @param dir
	 * @param pOperation
	 * @param options
	 * @throws IOException
	 */
	private static void parsingDirectory(Path dir, PathOperation pOperation, ParsingOptions...options) throws IOException {
		IOUtils ioUtils = parsingFile();
		try(Stream<Path> paths = Files.list(dir)) {
			paths.filter(p -> Parser.isClassFile(p) || p.toString().endsWith(".jar")).forEach(path -> ioUtils.execute(path, pOperation, options));
		}
	}
	
	/**
	 * Chooses which parsing method it should use.
	 * @param path
	 * @param pOperation
	 * @param options
	 * @throws IOException
	 */
	private static void chooseParser(Path path, PathOperation pOperation, ParsingOptions...options) throws IOException {
		if (!Files.isDirectory(path)) {
			if (!isClassFile(path)) {
				parsingJar().execute(path, pOperation, options);
			} else { parsingFile().execute(path, pOperation, options); }
		} else { parsingDirectory(path, pOperation, options); }
	}
	
	/**
	 * Verify's every requirement necessary to start parsing.
	 * @param path
	 * @param pOperation
	 * @param options
	 */
	private static void requires(Path path, PathOperation pOperation, ParsingOptions...options) {
		Objects.requireNonNull(path);
		Objects.requireNonNull(pOperation);
		Objects.requireNonNull(List.of(options));
		if (!Files.isDirectory(path) && !isClassFile(path) && !path.toString().endsWith(".jar")) {
			throw new IllegalArgumentException("path as to be a .class or a directory or a jar");
		}
	}
		
	/**
	 * Parses path and executes.
	 * @param path
	 * @param pOperation
	 * @param options
	 * @throws IOException
	 */
	public static void parse(Path path, PathOperation pOperation, ParsingOptions...options) throws IOException {
		requires(path, pOperation, options);
		chooseParser(path, pOperation, options);
		pOperation.execute(path, options);
	}
}
