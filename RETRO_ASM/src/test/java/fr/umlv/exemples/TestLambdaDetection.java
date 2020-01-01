package fr.umlv.exemples;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import fr.umlv.retro.features.Lambdas;
import fr.umlv.retro.observer.ObserverVisitor;
import fr.umlv.retro.parser.Parser;

public class TestLambdaDetection {
	@Test
	void testDetectLambda() {
		Path p = Paths.get("target/test-classes/fr/umlv/exemples/TestLambda.class");
    	ObserverVisitor ov = new ObserverVisitor();
    	
    	try {
			Parser.parserRead(p, ov);
		} catch (IOException e) {
			throw new AssertionError(e);
		}
    	
    	Set<String> set = Set.of(
    			"LAMBDA at TestLambda.testLambda()V (TestLambda.java:9): lambda java/util/function/IntBinaryOperator capture [] calling TestLambda.lambda$testLambda$0(II)I",
    			"LAMBDA at TestLambda.testLambda2()V (TestLambda.java:15): lambda java/util/function/IntUnaryOperator capture [I] calling TestLambda.lambda$testLambda2$1(II)I",
    			"LAMBDA at TestLambda.testLambda3()V (TestLambda.java:21): lambda java/util/function/Function capture [D] calling TestLambda.lambda$testLambda3$2(DLjava/lang/Integer;)Ljava/lang/Double;",
    			"LAMBDA at TestLambda.testMethodRef()V (TestLambda.java:27): lambda java/util/function/IntBinaryOperator capture [] calling java/lang/Integer.sum(II)I",
    			"LAMBDA at TestLambda.testMethodRef2()V (TestLambda.java:32): lambda java/util/function/BiFunction capture [] calling java/lang/Integer.sum(II)I",
    			"LAMBDA at TestLambda.testMethodRef3()V (TestLambda.java:41): lambda java/util/function/IntUnaryOperator capture [] calling TestLambda.methodRef(D)B");
    	assertEquals(set, ov.displayFeatureHistory(Lambdas.class).lines().collect(Collectors.toSet()));
	}
}
