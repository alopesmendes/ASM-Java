package fr.umlv.retro.observer;

import java.util.ArrayList;
import java.util.stream.Collectors;

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

public class ObserverVisitor extends ClassVisitor {
	private final ObserverHistory observerHistory = new ObserverHistory();
	private final FeaturesHistory featuresHistory = new FeaturesHistory();
	private String className;
	private final ArrayList<String> members = new ArrayList<>();
	
	public ObserverVisitor(int api) {
		super(api);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName,
			String[] interfaces) {
		className = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public void visitNestHost(String nestHost) {
		members.add(className);
		featuresHistory.add(Nestmates.create(className, nestHost));
		observerHistory.onMessageReceived(Nestmates.class, featuresHistory.getLast().toString());
		
		super.visitNestHost("nest host:"+nestHost);
	}
	
	@Override
	public void visitNestMember(String nestMember) {
		featuresHistory.add(Nestmates.create(className, nestMember, members));
		observerHistory.onMessageReceived(Nestmates.class, featuresHistory.getLast().toString());
		super.visitNestMember(nestMember);
	}
	
	private MethodVisitor methodVisitor(int api, MethodVisitor mv, String methodDescriptor) {
		return new MethodVisitor(Opcodes.ASM7, mv) {		
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
				if (Feature.detect(name + descriptor, "addSuppressed(Ljava/lang/Throwable;)V")) {
					featuresHistory.add(TryWithRessources.create(className, methodDescriptor, onMethod, line));
					observerHistory.onMessageReceived(TryWithRessources.class, featuresHistory.getLast().toString());
				}
				onMethod = owner;	        				
				super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
			}
			
			@Override
			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
					Object... bootstrapMethodArguments) {
				if (Feature.detect(name, "makeConcatWithConstants")) {
					featuresHistory.add(Concat.create(className, methodDescriptor, line, bootstrapMethodArguments[0].toString().split("")));
					observerHistory.onMessageReceived(Concat.class, featuresHistory.getLast().toString());
				} else if (Feature.detect(bootstrapMethodHandle.getOwner(), "java/lang/invoke/LambdaMetafactory")) {
					featuresHistory.add(Lambdas.create(className, methodDescriptor, descriptor, bootstrapMethodArguments[1].toString(), line));
					observerHistory.onMessageReceived(Lambdas.class, featuresHistory.getLast().toString());
				}
				super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
			}
		};	
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		return 	methodVisitor(Opcodes.ASM7, 
				super.visitMethod(access, name, descriptor, signature, exceptions),
				name+descriptor);	
	}
	
	public String displayFeatureHistory(Class<? extends Feature> typeFeature) {
		return observerHistory.get(typeFeature).stream().collect(Collectors.joining("\n"));
	}
	
	@Override
	public String toString() {
		return observerHistory.toString();
	}
}
