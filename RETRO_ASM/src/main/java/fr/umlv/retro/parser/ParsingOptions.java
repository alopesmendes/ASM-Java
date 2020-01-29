package fr.umlv.retro.parser;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import fr.umlv.retro.features.Feature;
import fr.umlv.retro.options.Option;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ParsingOptions {
	private final List<Class<? extends Feature>> features;
	private final boolean isInfos;
	private final boolean isTarget;
	private final int version;


	public ParsingOptions(List<Class<? extends Feature>> features, boolean isInfos, boolean isTarget, int version) {
		this.features = features;
		this.isInfos = isInfos;
		this.isTarget = isTarget;
		this.version = version;
	}




	/*

	 */
	static ClassWriter newClassWriter(ClassReader classReader, ParsingOptions...options) {
		Objects.requireNonNull(classReader);
		Objects.requireNonNull(Set.of(options));
		return new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
	}
	/*

	 */
	Optional<Integer> version(){
		return isTarget? Optional.of(version) : Optional.empty();
	}
}
