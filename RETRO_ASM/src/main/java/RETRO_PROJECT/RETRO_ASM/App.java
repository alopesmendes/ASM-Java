package RETRO_PROJECT.RETRO_ASM;

import java.io.IOException;
import java.nio.file.Paths;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import RETRO_PROJECT.PARSER.Parser;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    	
    	ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) {
    		@Override
    		public void visit(int version, int access, String name, String signature, String superName,
    				String[] interfaces) {
    			System.out.println("Version: " + (version - 44) + " name: " + name + " super: " + superName );
    			super.visit(version, access, name, signature, superName, interfaces);
    		}
    		
    		@Override
    		public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
    				String[] exceptions) {
    			
    			System.out.println("\t" + name + " " + descriptor);
    			return super.visitMethod(access, name, descriptor, signature, exceptions);
    		}
    	};
    	
    	
    	
    	Parser.parserJar(Paths.get("../yo.jar"), visitor);
    	System.out.println();
    	Parser.parserFile(Paths.get("../RetroTest"), visitor);
    }
}
