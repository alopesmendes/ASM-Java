package RETRO_PROJECT.RETRO_ASM;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import RETRO_PROJECT.PARSER.Parser;


public class ExempleWrite {
	
	static class Load extends ClassLoader {
		public Class<?> defineClass(String name, byte[] b) {
			return defineClass(name, b, 0, b.length);
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		var visitor = new ClassVisitor(Opcodes.ASM7) {
			@Override
			public void visit(int version, int access, String name, String signature, String superName,
					String[] interfaces) {
				System.out.println("Version: " + (version - 44) + " name: " + name + " super: " + superName );
				super.visit(version, access, name, signature, superName, interfaces);
			}
		};
		
		var readers = Parser.parseReader(Paths.get("../RetroTest"));
		var cw = new ClassWriter(0);
		cw.visit(Opcodes.V13, Opcodes.ACC_PUBLIC,
				"../RetroTest/bin/fr/umlv/exemples/Exemple2",
				null, "java/lang/Object", null);
		cw.visitField(Opcodes.ACC_PUBLIC + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,
				"CHAMP", Type.INT_TYPE.getDescriptor(), null, 5).visitEnd();
		cw.visitEnd();
		var b = cw.toByteArray();
		var l = new Load();
		Files.write(Paths.get("../RetroTest/bin/fr/umlv/exemples/Exemple2.class"), b);
		
	}
}
