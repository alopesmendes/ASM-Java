package fr.umlv.exemples;

public class TestDetectTry {
	/*@Test
	void testDetectLambda() {
		Path p = Paths.get("target/test-classes/fr/umlv/exemples");
    	ObserverVisitor ov = new ObserverVisitor(new ClassWriter(0));
    	
    	try {
			Parser.parse(p, PathOperation.create());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
    	
    	Set<String> set = Set.of(
    			"TRY_WITH_RESOURCES at TestTryWithResource.testTryWithResources()V (TestTryWithResource.java:17): try-with-resources on java/io/Closeable",
    			"TRY_WITH_RESOURCES at TestTryWithResource.testTryWithResources2()V (TestTryWithResource.java:29): try-with-resources on TestTryWithResource$2");
    	Set<String> set2 = Set.of(
    			"NESTMATES at TestTryWithResource$4 (TestTryWithResource.java): nestmate of TestTryWithResource",
    			"NESTMATES at TestTryWithResource$3 (TestTryWithResource.java): nestmate of TestTryWithResource",
    			"NESTMATES at TestTryWithResource$1 (TestTryWithResource.java): nestmate of TestTryWithResource",
    			"NESTMATES at TestTryWithResource$2 (TestTryWithResource.java): nestmate of TestTryWithResource",
    			"NESTMATES at TestTryWithResource (TestTryWithResource.java): nest host TestTryWithResource members [TestTryWithResource$4, TestTryWithResource$3, TestTryWithResource$2, TestTryWithResource$1]"
    	);
    	System.out.println(ov.displayFeatureHistory(Nestmates.class));
    	assertAll(
    		() -> assertEquals(set, ov.displayFeatureHistory(TryWithRessources.class).lines().collect(Collectors.toSet())),
    		() -> assertEquals(set2, ov.displayFeatureHistory(Nestmates.class).lines().collect(Collectors.toSet()))
    	);
	}*/
}
