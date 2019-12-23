package fr.umlv.retro.main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import fr.umlv.retro.features.Concat;
import fr.umlv.retro.features.Feature;
import fr.umlv.retro.features.FeaturesHistory;
import fr.umlv.retro.features.Lambdas;
import fr.umlv.retro.features.Nestmates;
import fr.umlv.retro.features.TryWithRessources;
import fr.umlv.retro.observer.ObserverHistory;
import fr.umlv.retro.parser.Parser;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
    	ObserverHistory history = new ObserverHistory();
    	FeaturesHistory fHistory = new FeaturesHistory();
    	
    	ClassVisitor visitor = new ClassVisitor(Opcodes.ASM7) {
    		private String className;
    		private ArrayList<String> members = new ArrayList<>();
    		
    		@Override
    		public void visit(int version, int access, String name, String signature, String superName,
    				String[] interfaces) {
    			className = name;
    			super.visit(version, access, name, signature, superName, interfaces);
    		}
    		
    		@Override
    		public void visitNestHost(String nestHost) {
    			members.add(className);
    			fHistory.add(Nestmates.create(className, nestHost));
        		history.onMessageReceived(Nestmates.class, fHistory.getLast().toString());
    			
    			super.visitNestHost("nest host:"+nestHost);
    		}
    		
    		@Override
    		public void visitNestMember(String nestMember) {
    			fHistory.add(Nestmates.create(className, nestMember, members));
    			history.onMessageReceived(Nestmates.class, fHistory.getLast().toString());
    			super.visitNestMember(nestMember);
    		}
    		
    		@Override
    		public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
    				String[] exceptions) {
    			String methodDescriptor = name+descriptor;
 
    			
    			MethodVisitor mv = new MethodVisitor(Opcodes.ASM7, super.visitMethod(access, name, descriptor, signature, exceptions)) {		
        			private int line;
        			private String onMethod = "";
        			
        			@Override
        			public void visitLineNumber(int line, Label start) {
        				this.line = line;
        				super.visitLineNumber(line, start);
        			}
    				
    				@Override
        			public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
        					boolean isInterface) {
        				if (Feature.detect(name + " " + descriptor, "addSuppressed (Ljava/lang/Throwable;)V")) {
        					fHistory.add(TryWithRessources.create(className, methodDescriptor, onMethod, line));
        					history.onMessageReceived(TryWithRessources.class, fHistory.getLast().toString());
        				}
        				if (Opcodes.INVOKEVIRTUAL == opcode || Opcodes.INVOKEINTERFACE == opcode) {
        					onMethod = owner;
        				}
        				super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        			}

        			
        			@Override
        			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
        					Object... bootstrapMethodArguments) {
        				if (Feature.detect(name, "makeConcatWithConstants")) {
        					fHistory.add(Concat.create(className, methodDescriptor, line, bootstrapMethodArguments[0].toString().split("")));
        					history.onMessageReceived(Concat.class, fHistory.getLast().toString());
        				} else if (Feature.detect(bootstrapMethodHandle.getOwner(), "java/lang/invoke/LambdaMetafactory")) {
        					fHistory.add(Lambdas.create(className, methodDescriptor, descriptor, bootstrapMethodArguments[1].toString(), line));
        					history.onMessageReceived(Lambdas.class, fHistory.getLast().toString());
        				}
        				super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        			}
        		};	
        		return mv;	
    		}
    	};

    	Parser.parserRead(Paths.get("../Yo/src/concat/lambda"), visitor);
    	System.out.println(history);
    }
}
