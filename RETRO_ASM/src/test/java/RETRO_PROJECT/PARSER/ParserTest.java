package RETRO_PROJECT.PARSER;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class ParserTest {
	
	@Test @Tag("parameter")
	public void testParserWithNullPath() {
		ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) { };
		assertThrows(NullPointerException.class, () -> Parser.parser(null, visitor));
	}
	
	@Test  @Tag("parameter")
	public void testParserWithNullVisitor() {
		
		assertThrows(NullPointerException.class, () -> Parser.parser(Paths.get("../yo.jar"), null));
	}
	
	@Test @Tag("parameter")
	public void testInexistantPath() {
		ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) { };
		assertAll(
			() -> assertThrows(IOException.class, () -> Parser.parser(Paths.get(" "), visitor)),
			() -> assertThrows(IOException.class, () -> Parser.parser(Paths.get("Inexistant.class"), visitor)),
			() -> assertThrows(IOException.class, () -> Parser.parser(Paths.get("../Yo/src/Inexistant"), visitor)),
			() -> assertThrows(IOException.class, () -> Parser.parser(Paths.get("../Inexistant.jar"), visitor))
		);
	}
	
	private static class CounterVisitor {
		private int value;
		
		public void inc() {
			value++;
		}
		
		public int value() {
			return value;
		}
	}
	
	@Test @Tag("FileClass")
	public void testParseFileClass() {
		CounterVisitor methodsCounter = new CounterVisitor();
		CounterVisitor fieldCounter = new CounterVisitor();
		CounterVisitor innerVisitor = new CounterVisitor();
		ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
					String[] exceptions) {
				methodsCounter.inc();
				return super.visitMethod(access, name, descriptor, signature, exceptions);
			}
			
			@Override
			public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
				fieldCounter.inc();
				return super.visitField(access, name, descriptor, signature, value);
			}
			
			@Override
			public void visitInnerClass(String name, String outerName, String innerName, int access) {
				innerVisitor.inc();
				super.visitInnerClass(name, outerName, innerName, access);
			}
			
			@Override
			public void visitNestHost(String nestHost) {
				innerVisitor.inc();
				super.visitNestHost(nestHost);
			}
		};
		try {
			Parser.parser(Paths.get("../Yo/bin/concat/Concat.class"), visitor);
		} catch (IOException e) { throw new AssertionError(e); }
		assertAll(
			() -> assertEquals(0, fieldCounter.value()),
			() -> assertEquals(2, methodsCounter.value()),
			() -> assertEquals(0, innerVisitor.value())
		);
	}
	
	@Test @Tag("DirectoryWithoutClass")
	public void testParseDirectoryWithoutClass() {
		CounterVisitor methodsCounter = new CounterVisitor();
		CounterVisitor fieldCounter = new CounterVisitor();
		CounterVisitor innerVisitor = new CounterVisitor();
		ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
					String[] exceptions) {
				methodsCounter.inc();
				return super.visitMethod(access, name, descriptor, signature, exceptions);
			}
			
			@Override
			public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
				fieldCounter.inc();
				return super.visitField(access, name, descriptor, signature, value);
			}
			
			@Override
			public void visitInnerClass(String name, String outerName, String innerName, int access) {
				innerVisitor.inc();
				super.visitInnerClass(name, outerName, innerName, access);
			}
			
			@Override
			public void visitNestHost(String nestHost) {
				innerVisitor.inc();
				super.visitNestHost(nestHost);
			}
		}; 
		
		try {
			Parser.parser(Paths.get("../Yo"), visitor);
		} catch (IOException e) { throw new AssertionError(e); }
		
		assertAll(
			() -> assertEquals(0, methodsCounter.value()),
			() -> assertEquals(0, fieldCounter.value()),
			() -> assertEquals(0, innerVisitor.value())
		);
	}
	
	@Test @Tag("DirectoryClass")
	public void testParseDirectoryClass() {
		CounterVisitor methodsCounter = new CounterVisitor();
		CounterVisitor fieldCounter = new CounterVisitor();
		CounterVisitor innerVisitor = new CounterVisitor();
		ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
					String[] exceptions) {
				methodsCounter.inc();
				return super.visitMethod(access, name, descriptor, signature, exceptions);
			}
			
			@Override
			public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
				fieldCounter.inc();
				return super.visitField(access, name, descriptor, signature, value);
			}
			
			@Override
			public void visitInnerClass(String name, String outerName, String innerName, int access) {
				innerVisitor.inc();
				super.visitInnerClass(name, outerName, innerName, access);
			}
			
			@Override
			public void visitNestHost(String nestHost) {
				innerVisitor.inc();
				super.visitNestHost(nestHost);
			}
		};
		
		
		try {
			Parser.parser(Paths.get("../Yo/bin/concat"), visitor);
		} catch (IOException e) { throw new AssertionError(e); }
		
		assertAll(
			() -> assertEquals(0, fieldCounter.value()),
			() -> assertEquals(2, methodsCounter.value()),
			() -> assertEquals(0, innerVisitor.value())
		);
	}
	
	@Test @Tag("JarClass")
	public void testParseJarClass() {
		CounterVisitor methodsCounter = new CounterVisitor();
		CounterVisitor fieldCounter = new CounterVisitor();
		CounterVisitor innerVisitor = new CounterVisitor();
		ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) {
			@Override
			public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
					String[] exceptions) {
				methodsCounter.inc();
				return super.visitMethod(access, name, descriptor, signature, exceptions);
			}
			
			@Override
			public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
				fieldCounter.inc();
				return super.visitField(access, name, descriptor, signature, value);
			}
			
			@Override
			public void visitInnerClass(String name, String outerName, String innerName, int access) {
				innerVisitor.inc();
				super.visitInnerClass(name, outerName, innerName, access);
			}
			
			@Override
			public void visitNestHost(String nestHost) {
				innerVisitor.inc();
				super.visitNestHost(nestHost);
			}
		};
		
		
		try {
			Parser.parser(Paths.get("../Yo.jar"), visitor);
		} catch (IOException e) { throw new AssertionError(e); }
		
		assertAll(
			() -> assertEquals(0, fieldCounter.value()),
			() -> assertEquals(5, methodsCounter.value()),
			() -> assertEquals(1, innerVisitor.value())
		);
	}
	
	
	
	
	
	
}
