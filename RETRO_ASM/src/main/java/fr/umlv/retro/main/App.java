package fr.umlv.retro.main;

import java.io.IOException;
import java.nio.file.Paths;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import fr.umlv.retro.features.Concat;
import fr.umlv.retro.features.Lambdas;
import fr.umlv.retro.features.TryWithRessources;
import fr.umlv.retro.parser.Parser;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    	final TryWithRessources t = new TryWithRessources();
    	final Concat concat = new Concat();
    	final Lambdas lambdas = new Lambdas();
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
        				super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        			}

        			
        			@Override
        			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
        					Object... bootstrapMethodArguments) {
        				if (concat.detectFeature(name)) {
        					System.out.println("\t -> concat " + name + " " + descriptor);
        				}
        				if (lambdas.detectFeature(bootstrapMethodHandle.getOwner())) {
        					System.out.println("\t -> lambda " + name + " " + descriptor);
        				}
        				super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        			}
        		};	
        		return mv;
    			
    		}
    	};

    	Parser.parserRead(Paths.get("../Yo/bin/concat/lambda/ConcatLambda.class"), visitor);
    	
    }
}
