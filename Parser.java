package fr.umlv.retro.parser;

import java.io.IOError;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import fr.umlv.retro.features.FeatureVisitor;
import fr.umlv.retro.observer.ObserverVisitor;

public class Parser { 
	
	@FunctionalInterface
	private static interface IOUtils {
		void execute(Path path, NoName noName, ParsingOptions... options);
		
	}
	
	@FunctionalInterface
	private static interface ParserWriter {
		void write(int version, int flags);
		
		static ParserWriter writeFile(Path path) {
			return (version, flags) -> {
				try {
					ClassReader classReader = parserFile(path);
					ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
					FeatureVisitor visitor = FeatureVisitor.create(new ObserverVisitor(classWriter));
					classReader.accept(visitor, ClassReader.EXPAND_FRAMES);
					Files.write(path, classWriter.toByteArray());
				} catch (IOException e) { throw new IOError(e); }
			};
		}
		
		static ParserWriter writeEntry(ZipOutputStream zOutputStream, Entry<ZipEntry, byte[]> entry) {
			return (version, flags) -> {
				try {
					byte[] bytes;
					if (isClassFile(entry.getKey())) {
						ClassReader classReader = new ClassReader(entry.getValue());
						ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
						FeatureVisitor featureVisitor = FeatureVisitor.create(new ObserverVisitor(classWriter));
						classReader.accept(featureVisitor, ClassReader.EXPAND_FRAMES);
						bytes = classWriter.toByteArray();
						
					} else {
						bytes = entry.getValue();
					}
					ZipEntry zipEntry = new ZipEntry(entry.getKey().getName());
					zOutputStream.putNextEntry(zipEntry);
					zOutputStream.write(bytes);
					zOutputStream.closeEntry();
				} catch (IOException e) {
					throw new IOError(e);
				}
			};
		}
	} 
	
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
	private static List<ParserWriter> parserDirectory(Path path) throws IOException {
		try(Stream<Path> list = Files.list(path)) {
			return 	list.filter(Parser::isClassFile).map(p -> ParserWriter.writeFile(p)).
					collect(Collectors.toList());
		}
	}
	
	private static byte[] valueOf(ZipFile zipFile, ZipEntry entry) {
		try {
			return zipFile.getInputStream(entry).readAllBytes();
		} catch (IOException e) {
			throw new IOError(e);
		}
	}
	
	private static Map<ZipEntry, byte[]> elementsOfZip(Path path) throws ZipException, IOException {
		try (ZipFile zipFile = new ZipFile(path.toFile())) {
			return zipFile.stream().collect(Collectors.toMap((ZipEntry x) -> x, (ZipEntry y) -> valueOf(zipFile, y)));
		}	
	}
	
	private static void rewriteZipEntry(ZipOutputStream zOutputStream, Entry<ZipEntry, byte[]> entry) {
		/*zOutputStream.putNextEntry(entry.getKey());
		zOutputStream.write(entry.getValue());
		zOutputStream.closeEntry();*/
		var w = ParserWriter.writeEntry(zOutputStream, entry);
		w.write(Opcodes.V13, 0);
	}

	/**
	 * Creates a List of ClassReader each ClassReader relied to a class file.
	 * @param path
	 * @return List of ClassReader according to a jar.
	 * @throws IOException
	 */
	private static List<ParserWriter> parserJar(Path path) throws IOException {
		var h = elementsOfZip(path);
		try(OutputStream out = Files.newOutputStream(path);
			ZipOutputStream zOutputStream = new ZipOutputStream(out)) {
			h.entrySet().stream().forEach(e -> rewriteZipEntry(zOutputStream, e));
		}			
		return new ArrayList<>();
	}
	
	/**
	 * Chooses how to parse our path and return the List of ClassReader.
	 * @param path
	 * @return List of ClassReader.
	 * @throws IOException
	 */
	private static List<ParserWriter> chooseParser(Path path) throws IOException {
		if (!Files.isDirectory(path)) {
			if (path.toString().endsWith(".jar")) { return parserJar(path); }
			else { return List.of(ParserWriter.writeFile(path)); }
		} 
		else { return parserDirectory(path); }
	}
	
	/**
	 * Parses a given path and only reads.
	 * @param path
	 * @param visitor
	 * @throws IOException
	 */
	public static void parserRead(Path path, ObserverVisitor visitor) throws IOException {
		Objects.requireNonNull(path);
		Objects.requireNonNull(visitor);
		
		List<ParserWriter> readers = chooseParser(path);
		readers.forEach(r -> r.write(Opcodes.V13, 0));
	}
	
	/**
	 * Rewrites features according to version.
	 * @param version
	 * @param path
	 * @param visitor
	 * @throws IOException
	 */
	public static void parserWriter(int version, Path path, ObserverVisitor visitor) throws IOException {
		Objects.requireNonNull(path);
		Objects.requireNonNull(visitor);
		
	}
	
	public static void parser(Path path, NoName noName, ParsingOptions...options) throws IOException {
		Objects.requireNonNull(path);
		Objects.requireNonNull(noName);
		Objects.requireNonNull(options);
		List<ParserWriter> readers = chooseParser(path);
		readers.forEach(r -> r.write(Opcodes.V13, 0));
	}

}
