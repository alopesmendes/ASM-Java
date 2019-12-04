package RETRO_PROJECT.RETRO_ASM;

import java.io.IOException;
import java.nio.file.Paths;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import fr.umlv.parser.Parser;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    	byte[] b = Parser.parser(Paths.get("RetroTest/bin/fr/umlv/exemples/Exemple.class"));
    	
    	ClassReader reader = new ClassReader(b);
    	ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) {
    		@Override
    		public void visit(int version, int access, String name, String signature, String superName,
    				String[] interfaces) {
    			System.out.println("Version: " + (version - 44));
    			super.visit(version, access, name, signature, superName, interfaces);
    		}
    	};
    	
    	reader.accept(visitor, ClassReader.EXPAND_FRAMES);
        System.out.println( "Hello World!" );
    }
}
