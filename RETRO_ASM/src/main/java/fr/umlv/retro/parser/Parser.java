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

import fr.umlv.retro.options.OptionParsing;

/**
 * The Parser class will parse the given path.
 * Path as to be a .class, directory of .class or a jar.
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public class Parser {
	
	private Parser() {
		
	}
	
	@FunctionalInterface
	private static interface IOUtils {
		/**
		 * @param path a Path
		 * @param operation a PathOperation
		 * @param options a OptionParsing.
		 */
		void execute(Path path, PathOperation operation, OptionParsing options);
	}
	
	/**
	 * With a given path determines if is .class.
	 * @param path a Path.
	 * @return true if is .class
	 */
	static boolean isClassFile(Path path) {
		return !Files.isDirectory(path) && path.toString().endsWith(".class");
	}
	
	/**
	 * With a given zipEntry if is .class.
	 * @param zipEntry a ZipEntry.
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
	 * @param path a Path.
	 * @param jStream a JarInputStream.
	 * @param pOperation a PathOperation.
	 * @param options a OptionParsing
	 * @throws IOException if the method getNextEntry throws an IOException.
	 */
	private static void parsingEntry(Path path, JarInputStream jStream, PathOperation pOperation, OptionParsing options) throws IOException {
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
	 * @param dir a Path.
	 * @param pOperation a PathOperation.
	 * @param options a OptionParsing.
	 * @throws IOException if the method Files.list throws an IOException.
	 */
	private static void parsingDirectory(Path dir, PathOperation pOperation, OptionParsing options) throws IOException {
		IOUtils ioUtils = parsingFile();
		try(Stream<Path> paths = Files.list(dir)) {
			paths.filter(p -> Parser.isClassFile(p) || p.toString().endsWith(".jar")).forEach(path -> ioUtils.execute(path, pOperation, options));
		}
	}
	
	/**
	 * Chooses which parsing method it should use.
	 * @param path a path.
	 * @param pOperation a PathOperation.
	 * @param options a OptionParsing.
	 * @throws IOException if the code throws an IOException.
	 */
	private static void chooseParser(Path path, PathOperation pOperation, OptionParsing options) throws IOException {
		if (!Files.isDirectory(path)) {
			if (!isClassFile(path)) {
				parsingJar().execute(path, pOperation, options);
			} else { parsingFile().execute(path, pOperation, options); }
		} else { parsingDirectory(path, pOperation, options); }
	}
	
	/**
	 * Verify's every requirement necessary to start parsing.
	 * @param path a Path.
	 * @param pOperation a PathOperation.
	 * @param options a OptionParsing.
	 */
	private static void requires(Path path, PathOperation pOperation, OptionParsing options) {
		Objects.requireNonNull(path);
		Objects.requireNonNull(pOperation);
		Objects.requireNonNull(List.of(options));
		if (!Files.isDirectory(path) && !isClassFile(path) && !path.toString().endsWith(".jar")) {
			throw new IllegalArgumentException("path as to be a .class or a directory or a jar");
		}
	}
		
	/**
	 * Parses path and executes.
	 * @param path a Path.
	 * @param pOperation a PathOperation.
	 * @param options a OptionParsing.
	 * @throws IOException while parsing it may throws an IOException.
	 */
	public static void parse(Path path, PathOperation pOperation, OptionParsing options) throws IOException {
		requires(path, pOperation, options);
		chooseParser(path, pOperation, options);
		pOperation.execute(path, options);
	}
}
