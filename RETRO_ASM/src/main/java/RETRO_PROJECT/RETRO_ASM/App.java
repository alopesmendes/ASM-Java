package RETRO_PROJECT.RETRO_ASM;

import java.io.IOException;
import java.nio.file.Paths;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import RETRO_PROJECT.Features.Concat;
import RETRO_PROJECT.Features.TryWithRessources;
import RETRO_PROJECT.PARSER.Parser;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    	final TryWithRessources t = new TryWithRessources();
    	final Concat concat = new Concat();
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
    			
    			MethodVisitor mv = new MethodVisitor(Opcodes.ASM7, super.visitMethod(access, name, descriptor, signature, exceptions)) {		
        			@Override
        			public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
        					boolean isInterface) {
        
        				if (t.detectFeature(name + " " + descriptor)) {
        					System.out.println("\t -> try " + owner + " " + name + " " + descriptor);
        				}
        				System.out.println("\t" + name + " " + descriptor);
        				super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        			}
        			
        			@Override
        			public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        				System.out.println("\t\t" + name + " " + descriptor);
        				super.visitFieldInsn(opcode, owner, name, descriptor);
        			}
        			
        			@Override
        			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
        					Object... bootstrapMethodArguments) {
        				if (concat.detectFeature(name)) {
        					System.out.println("\t -> concat " + name + " " + descriptor);
        				}
        				super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        			}
        		};	
        		return mv;
    			
    		}
    	};


    	Parser.parser(Paths.get("../yo.jar"), visitor);
    	System.out.println();
    	Parser.parser(Paths.get("../Yo/bin/concat"), visitor);
    	System.out.println();
    	Parser.parser(Paths.get("../Yo/bin/concat/lambda/ConcatLambda.class"), visitor);
    	
    }
}
