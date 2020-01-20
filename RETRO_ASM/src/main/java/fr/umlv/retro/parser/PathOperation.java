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
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import fr.umlv.retro.features.FeatureVisitor;
import fr.umlv.retro.observer.ObserverVisitor;

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
	void execute(Path path, ParsingOptions...options) throws IOException;
	
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
		
		private ClassWriter executeClass(String name, ParsingOptions...options) {
			ClassReader classReader = map.get(name);
			ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
			FeatureVisitor featureVisitor = FeatureVisitor.create(new ObserverVisitor(classWriter), options);
			classReader.accept(featureVisitor, ClassReader.EXPAND_FRAMES);
			features.add(featureVisitor);
			return classWriter;
		}
		
		private void executeFile(Path path, ParsingOptions...options) throws IOException {
			try(InputStream in = Files.newInputStream(path)) {
				ClassWriter classWriter = executeClass(path.toString(), options);
				Files.write(path, classWriter.toByteArray());
			}
		}
		
		private void executeDirectory(Path path, ParsingOptions...options) throws IOException {
			try (Stream<Path> paths = Files.list(path)) {
				paths.filter(Parser::isClassFile).forEach(p -> {
					try {
						executeFile(p, options);
					} catch (IOException e) { throw new IOError(e); }
				});
			}
		}
		
		private byte[] byteOf(ZipInputStream zipInputStream, ZipEntry entry, ParsingOptions...options) throws IOException {
			if (Parser.isClassFile(entry)) {
				ClassWriter classWriter = executeClass(entry.getName(), options);
				return classWriter.toByteArray();
			}
			return new ZipInputStream(zipInputStream).readAllBytes();
		}
		
		private Map<ZipEntry, byte[]> mappingEntry(ZipInputStream zipInputStream, ParsingOptions...options) throws IOException {
			return Stream.iterate(zipInputStream.getNextEntry(), p -> p != null, p -> {
				try {
					return p = zipInputStream.getNextEntry();
				} catch (IOException e) { throw new IOError(e); }
			}).collect(Collectors.toMap((ZipEntry x) -> x, (ZipEntry y) -> {
				try {
					return byteOf(zipInputStream, y, options);
				} catch (IOException e) { throw new IOError(e); }
			}));
		}
		
		private void putInOutputStream(Entry<ZipEntry, byte[]> e, ZipOutputStream zStream) throws IOException {
			ZipEntry zipEntry = new ZipEntry(e.getKey().getName());
			zStream.putNextEntry(zipEntry);
			zStream.write(e.getValue());
			zStream.closeEntry();
		}
		
		private void executeEntry(Path path, ZipInputStream zipInputStream, ParsingOptions...options) throws IOException {
			Map<ZipEntry, byte[]> entries = mappingEntry(zipInputStream, options);
			try(OutputStream out = Files.newOutputStream(path);
				ZipOutputStream zStream = new ZipOutputStream(out)) {
				entries.entrySet().forEach(entry -> {
					try {
						putInOutputStream(entry, zStream);
					} catch (IOException e) { throw new IOError(e); }
				});
			}
		}
		
		private void executeJar(Path path, ParsingOptions...options) throws IOException {
			try(InputStream in = Files.newInputStream(path);
				ZipInputStream zipInputStream = new ZipInputStream(in)) {
				executeEntry(path, zipInputStream, options);
			}
		}
		
		@Override
		public void execute(Path path, ParsingOptions... options) throws IOException {
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
