module retro {
	exports fr.umlv.retro.options;
	exports fr.umlv.retro.main;
	exports fr.umlv.retro.parser;
	exports fr.umlv.retro.observer;
	
	
	exports fr.umlv.retro.features to
		fr.umlv.retro.observer;

	requires transitive org.objectweb.asm;

    uses org.objectweb.asm.ClassVisitor;
	uses org.objectweb.asm.ClassReader;
	uses org.objectweb.asm.ClassWriter;
	uses org.objectweb.asm.MethodVisitor;
	uses org.objectweb.asm.Type;
	uses org.objectweb.asm.Handle;
}