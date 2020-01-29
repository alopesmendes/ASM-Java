package fr.umlv.retro.parser;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import fr.umlv.retro.features.FeatureVisitor;
import fr.umlv.retro.options.OptionParsing;

public interface PathOperation {
	/**
	 * Stocks according to a name of a .class it's classReader.
	 * @param name
	 * @param classReader
	 */
	void stock(String name, ClassReader classReader); 
	
	/**
	 * Managed by the given options.
	 * Executes all the operations in the given path.
	 * The path can only be a directory, a file .class and a jar.
	 * @param path
	 * @param options
	 * @throws IOException
	 */
	void execute(Path path, OptionParsing options) throws IOException;
	
	/**
	 * @return all the featureVisitor created after execute.
	 */
	List<FeatureVisitor> allFeatures();
	
	static PathOperation create() {
		return new NoNameImpl();
	}

	static class NoNameImpl implements PathOperation {
		private final HashMap<String, ClassReader> map = new HashMap<>();
		private final ArrayList<FeatureVisitor> features = new ArrayList<>();
		@Override
		public void stock(String name, ClassReader classReader) {
			Objects.requireNonNull(name);
			Objects.requireNonNull(classReader);
			map.put(name, classReader);		
		}
		
		/**
		 * Add a new FeatureVisitor to features and return a classWriter.
		 * @param name
		 * @param options
		 * @return the classWriter create with options.
		 */
		private ClassWriter executeClass(String name, OptionParsing options) {
			ClassReader classReader = map.get(name);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			FeatureVisitor featureVisitor = FeatureVisitor.create(classWriter, options);
			classReader.accept(featureVisitor, ClassReader.EXPAND_FRAMES);
			features.add(featureVisitor);
			return classWriter;
		}
		
		/**
		 * Executes a file.
		 * @param path
		 * @param options
		 * @throws IOException
		 */
		private void executeFile(Path path, OptionParsing options) throws IOException {
			try(InputStream in = Files.newInputStream(path)) {
				ClassWriter classWriter = executeClass(path.toString(), options);
				Files.write(path, classWriter.toByteArray());
			}
		}
		
		/**
		 * Executes all the files .class or .jar of a directory.
		 * @param path
		 * @param options
		 * @throws IOException
		 */
		private void executeDirectory(Path path, OptionParsing options) throws IOException {
			try (Stream<Path> paths = Files.list(path)) {
				paths.filter(p -> Parser.isClassFile(p) || p.toString().endsWith(".jar")).forEach(p -> {
					try {
						if (Parser.isClassFile(p)) {
							executeFile(p, options);
						} else { executeJar(p, options); }
					} catch (IOException e) { throw new IOError(e); }
				});
			}
		}
		
		/**
		 * If entry is a .class return the byte array of the corresponding ClassWriter.
		 * Otherwise creates a new ZipInputStream from zipInputStream and reads all bytes.
		 * @param jarInputStream
		 * @param entry
		 * @param options
		 * @return the an byte array.
		 * @throws IOException
		 */
		private byte[] byteOf(JarInputStream jarInputStream, ZipEntry entry, OptionParsing options) throws IOException {
			if (Parser.isClassFile(entry)) {
				ClassWriter classWriter = executeClass(entry.getName(), options);
				return classWriter.toByteArray();
			}
			return new ZipInputStream(jarInputStream).readAllBytes();
		}
		
		/**
		 * Creates a Map with as key every zipEntry and their byte array.
		 * @param jarInputStream
		 * @param options
		 * @return a map of ZipEntry and byte[].
		 * @throws IOException
		 */
		private Map<ZipEntry, byte[]> mappingEntry(JarInputStream jarInputStream, OptionParsing options) throws IOException {
			return Stream.iterate(jarInputStream.getNextEntry(), p -> p != null, p -> {
				try {
					return p = jarInputStream.getNextEntry();
				} catch (IOException e) { throw new IOError(e); }
			}).collect(Collectors.toMap((ZipEntry x) -> x, (ZipEntry y) -> {
				try {
					return byteOf(jarInputStream, y, options);
				} catch (IOException e) { throw new IOError(e); }
			}));
		}
		
		/**
		 * Writes the entry in zStream.
		 * @param e
		 * @param jStream
		 * @throws IOException
		 */
		private void write(Entry<ZipEntry, byte[]> e, JarOutputStream jStream) throws IOException {
			ZipEntry zipEntry = new ZipEntry(e.getKey().getName());
			jStream.putNextEntry(zipEntry);
			jStream.write(e.getValue());
			jStream.closeEntry();
		}
		
		/**
		 * Executes all the entry of a jar.
		 * @param path
		 * @param jarInputStream
		 * @param options
		 * @throws IOException
		 */
		private void executeInsideJar(Path path, JarInputStream jarInputStream, OptionParsing options) throws IOException {
			Map<ZipEntry, byte[]> entries = mappingEntry(jarInputStream, options);
			try(OutputStream out = Files.newOutputStream(path);
				JarOutputStream zStream = new JarOutputStream(out, jarInputStream.getManifest())) {
				entries.entrySet().forEach(entry -> {
					try {
						write(entry, zStream);
					} catch (IOException e) { throw new IOError(e); }
				});
			}
		}
		
		/**
		 * Executes the jar.
		 * @param path
		 * @param options
		 * @throws IOException
		 */
		private void executeJar(Path path, OptionParsing options) throws IOException {
			try(InputStream in = Files.newInputStream(path);
				JarInputStream jarInputStream = new JarInputStream(in)) {
				executeInsideJar(path, jarInputStream, options);
			}
		}
		
		@Override
		public void execute(Path path, OptionParsing options) throws IOException {
			Objects.requireNonNull(path);
			Objects.requireNonNull(List.of(options));
			if (Parser.isClassFile(path)) {
				executeFile(path, options); 
			} else if (path.toString().endsWith(".jar")) {
				executeJar(path, options);
			} else { executeDirectory(path, options); }	
		}

		@Override
		public List<FeatureVisitor> allFeatures() {
			return features;
		}
		
	}
}
