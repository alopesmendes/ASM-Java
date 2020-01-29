package fr.umlv.exemples;

public class TestDetectConcat {
	/*@Test
	void testDetectConcat() {
		Path p = Paths.get("target/test-classes/fr/umlv/exemples/TestConcat.class");
    	ObserverVisitor ov = new ObserverVisitor(new ClassWriter(0));
    	
    	try {
			Parser.parse(p, PathOperation.create());
		} catch (IOException e) {
			throw new AssertionError(e);
		}
    	
    	Set<String> set = Set.of(
    			"CONCATENATION at TestConcat.testConcat2()V (TestConcat.java:18): pattern %1 %1 %1",
    			"CONCATENATION at TestConcat.testConcat3()V (TestConcat.java:24): pattern %1%1",
    			"CONCATENATION at TestConcat.testConcat()V (TestConcat.java:10): pattern uri %1 value %1.");
    	
    	assertEquals(set, ov.displayFeatureHistory(Concat.class).lines().collect(Collectors.toSet()));
	}*/
}
