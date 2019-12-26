package fr.umlv.retro.observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import fr.umlv.retro.features.Concat;
import fr.umlv.retro.features.Feature;
import fr.umlv.retro.features.Lambdas;
import fr.umlv.retro.features.Nestmates;
import fr.umlv.retro.features.Record;
import fr.umlv.retro.features.TryWithRessources;

public class ObserverVisitor extends ClassVisitor {
	private final ObserverHistory observerHistory = new ObserverHistory();
	private final FeatureInfoMessage messages = FeatureInfoMessage.create();
	private String className;
	private String host = "";
	private final ArrayList<String> members = new ArrayList<>();
	
	public ObserverVisitor() {
		super(Opcodes.ASM7);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName,
			String[] interfaces) {
		className = name;
		if (Feature.detect("java/lang/Record", superName)) {
			observerHistory.onMessageReceived(Record.class, messages.infoOf(Record.class, name, name));
		}
	}
	
	@Override
	public void visitNestHost(String nestHost) {
		String[] array = nestHost.split("/");
		host = Arrays.stream(array).skip(array.length-1).findFirst().get();
		members.add(className);
	}
	
	@Override
	public void visitNestMember(String nestMember) {
		//System.out.println("\there members:"+members);
		observerHistory.onMessageReceived(Nestmates.class, messages.infoOf(Nestmates.class, nestMember, className, " nestMate of ", className));
	}
	
	private MethodVisitor methodVisitor(String methodDescriptor) {
		return new MethodVisitor(Opcodes.ASM7) {		
			private int line;
			private String onMethod = "";
			private boolean isTry;
			
			@Override
			public void visitLineNumber(int line, Label start) {
				this.line = line;
			}
						
			@Override
			public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
				isTry = true;
			}
			
			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
					boolean isInterface) {
				if (isTry && Feature.detect(name + descriptor, "addSuppressed(Ljava/lang/Throwable;)V")) {
					//TryWithRessources t = TryWithRessources.create(className, methodDescriptor, onMethod, line);
					//featuresHistory.add(t);
					observerHistory.onMessageReceived(TryWithRessources.class, messages.infoOf(TryWithRessources.class, className, methodDescriptor, line+"", onMethod));
					isTry = false;
				}
				onMethod = owner;	        				
			}
			
			
			@Override
			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
					Object... bootstrapMethodArguments) {
				if (Feature.detect(name, "makeConcatWithConstants")) {
					//Concat c = Concat.create(className, methodDescriptor, line, bootstrapMethodArguments[0].toString().split(""));
					//featuresHistory.add(c);
					observerHistory.onMessageReceived(Concat.class, messages.infoOf(Concat.class, className, methodDescriptor, line+"", bootstrapMethodArguments[0].toString()));
				} else if (Feature.detect(bootstrapMethodHandle.getOwner(), "java/lang/invoke/LambdaMetafactory")) {
					//Lambdas l = Lambdas.create(className, methodDescriptor, descriptor, bootstrapMethodArguments[1].toString(), line);
					//featuresHistory.add(l);
					observerHistory.onMessageReceived(Lambdas.class, messages.infoOf(Lambdas.class, className, methodDescriptor, line+"", descriptor, bootstrapMethodArguments[1].toString()));
				}
			}
		};	
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		return 	methodVisitor(name+descriptor);	
	}
	
	@Override
	public void visitEnd() {
		if (host.isEmpty() || !className.endsWith(host)) {
			return;
		}
		List<String> m = members.stream().map(x -> x.split("/")).flatMap(x -> Arrays.stream(x).skip(x.length-1)).
					collect(Collectors.toList());
		String n = messages.infoOf(Nestmates.class, host, host, " nest host " + host + " members", m.toString());
		observerHistory.onMessageReceived(Nestmates.class, n);
	}
	
	public String displayFeatureHistory(Class<? extends Feature> typeFeature) {
		return observerHistory.infoOf(typeFeature).stream().collect(Collectors.joining("\n"));
	}
	
	@Override
	public String toString() {
		return observerHistory.toString();
	}
}
