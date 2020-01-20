package fr.umlv.retro.parser;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public interface ParsingOptions {
	static ClassWriter newClassWriter(ClassReader classReader, ParsingOptions...options) {
		Objects.requireNonNull(classReader);
		Objects.requireNonNull(Set.of(options));
		return new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
	}
	
	Optional<Integer> version();
}
