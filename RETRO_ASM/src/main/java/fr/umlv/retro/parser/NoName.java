package fr.umlv.retro.parser;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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

public interface NoName {
	void stock(String name, ClassReader classReader); 
	
	void execute(Path path, ParsingOptions...options) throws IOException;
	
	static NoName create() {
		return new NoNameImpl();
	}

	static class NoNameImpl implements NoName {
		private final HashMap<String, ClassReader> map = new HashMap<>();
		
		@Override
		public void stock(String name, ClassReader classReader) {
			Objects.requireNonNull(name);
			Objects.requireNonNull(classReader);
			map.put(name, classReader);		
		}
		
		private void executeFile(Path path, ParsingOptions...options) throws IOException {
			try(InputStream in = Files.newInputStream(path)) {
				ClassReader classReader = map.get(path.toString());
				ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
				FeatureVisitor featureVisitor = FeatureVisitor.create(new ObserverVisitor(classWriter), options);
				classReader.accept(featureVisitor, ClassReader.EXPAND_FRAMES);
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
		
		private byte[] byteOf(ZipInputStream zipInputStream, ZipEntry entry) throws IOException {
			if (Parser.isClassFile(entry)) {
				ClassReader classReader = map.get(entry.toString());
				ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
				FeatureVisitor featureVisitor = FeatureVisitor.create(new ObserverVisitor(classWriter));
				classReader.accept(featureVisitor, ClassReader.EXPAND_FRAMES);
				return classWriter.toByteArray();
			}
			return new ZipInputStream(zipInputStream).readAllBytes();
		}
		
		private Map<ZipEntry, byte[]> mappingEntry(ZipInputStream zipInputStream) throws IOException {
			return Stream.iterate(zipInputStream.getNextEntry(), p -> p != null, p -> {
				try {
					return p = zipInputStream.getNextEntry();
				} catch (IOException e) { throw new IOError(e); }
			}).collect(Collectors.toMap((ZipEntry x) -> x, (ZipEntry y) -> {
				try {
					return byteOf(zipInputStream, y);
				} catch (IOException e) { throw new IOError(e); }
			}));
		}
		
		private void putInOutputStream(Entry<ZipEntry, byte[]> e, ZipOutputStream zStream) throws IOException {
			ZipEntry zipEntry = new ZipEntry(e.getKey().getName());
			zStream.putNextEntry(zipEntry);
			zStream.write(e.getValue());
			zStream.closeEntry();
		}
		
		private void executeEntry(Path path, ZipInputStream zipInputStream) throws IOException {
			Map<ZipEntry, byte[]> entries = mappingEntry(zipInputStream);
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
				executeEntry(path, zipInputStream);
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
		
	}
}
