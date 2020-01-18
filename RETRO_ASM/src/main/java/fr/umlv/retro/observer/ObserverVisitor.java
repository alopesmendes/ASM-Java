package fr.umlv.retro.observer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
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
	private String host;
	private final HashMap<String, ArrayList<String>> members = new HashMap<>();
	
	public ObserverVisitor(ClassWriter cw) {
		super(Opcodes.ASM7, cw);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature, String superName,
			String[] interfaces) {
		className = name;
		String[] array = className.split("/");
		host = Arrays.stream(array).skip(array.length-1).findFirst().get();
		if (Feature.detect("java/lang/Record", superName)) {
			observerHistory.onMessageReceived(Record.class, messages.infoOf(Record.class, name, name));
		}
		cv.visit(version, access, name, signature, superName, interfaces);
	}
	
	@Override
	public void visitNestMember(String nestMember) {
		observerHistory.onMessageReceived(Nestmates.class, messages.infoOf(Nestmates.class, nestMember, className, " nestmate of", className));
		cv.visitNestMember(nestMember);
		ArrayList<String> m = members.getOrDefault(host, new ArrayList<>());
		m.add(nestMember);
		members.put(host, m);
	}
	
	private MethodVisitor methodVisitor(MethodVisitor methodVisitor,  String methodDescriptor) {
		return new MethodVisitor(api, methodVisitor) {		
			private int line;
			private String onMethod = "";
			private boolean isTry;
			private String msg = "";
			
			@Override
			public void visitCode() {
				mv.visitCode();
			}
			
			@Override
			public void visitLineNumber(int line, Label start) {
				this.line = line;
				mv.visitLineNumber(line, start);
			}
					
			@Override
			public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
				isTry = true;
				mv.visitTryCatchBlock(start, end, handler, type);
			}
			
			@Override
			public void visitMethodInsn(int opcode, String owner, String name, String descriptor,
					boolean isInterface) {
			
				if (isTry && msg.equals("close()V") && Feature.detect(name + descriptor, "addSuppressed(Ljava/lang/Throwable;)V")) {
					String s = messages.infoOf(TryWithRessources.class, className, methodDescriptor, line+"", onMethod);
					observerHistory.onMessageReceived(TryWithRessources.class, s);
					isTry = false;
				}
				onMethod = owner;
				msg = name+descriptor;
				mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
			}
			
			@Override
			public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle,
					Object... bootstrapMethodArguments) {
				if (Feature.detect(name, "makeConcatWithConstants")) {
					observerHistory.onMessageReceived(Concat.class, messages.infoOf(Concat.class, className, methodDescriptor, line+"", bootstrapMethodArguments[0].toString()));
				} else if (Feature.detect(bootstrapMethodHandle.getOwner(), "java/lang/invoke/LambdaMetafactory")) {
					observerHistory.onMessageReceived(Lambdas.class, messages.infoOf(Lambdas.class, className, methodDescriptor, line+"", descriptor, bootstrapMethodArguments[1].toString()));
				}
				mv.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
			}
			
			@Override
			public void visitEnd() {
				mv.visitEnd();
			}
	
		};	
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
			String[] exceptions) {
		return 	methodVisitor( 
				super.visitMethod(access, name, descriptor, signature, exceptions),
				name+descriptor);	
	}
	
	private void messageNestMembers() {
		if (host.isEmpty() || members.getOrDefault(host, new ArrayList<>()).isEmpty()) {
			return;
		}
		List<String> m = members.get(host).stream().map(x -> x.split("/")).flatMap(x -> Arrays.stream(x).skip(x.length-1)).
					collect(Collectors.toList());
		String n = messages.infoOf(Nestmates.class, host, host, " nest host " + host + " members", m.stream().
				sorted((x, y) -> y.compareTo(x)).collect(Collectors.toList()).toString());
		observerHistory.onMessageReceived(Nestmates.class, n);
	}

	@Override
	public void visitEnd() {
		messageNestMembers();
		cv.visitEnd();
		host = "";
	}
	
	/**
	 * @param typeFeature
	 * @return the messages according to a type of feature.
	 */
	public String displayFeatureHistory(Class<? extends Feature> typeFeature) {
		return observerHistory.infoOf(typeFeature).stream().collect(Collectors.joining("\n"));
	}
	
	@Override
	public String toString() {
		return observerHistory.toString();
	}
}
