package concat.nest;

public class TestNest {
	
	static class InnerClass {
		private int a = 4; 
		private static int b = 5;
		private final int c = 6;
		
		private void method() {
			TestNest nest = new TestNest();
			System.out.println(nest);
		}
	}
	
	private void method2() {
		InnerClass inner = new InnerClass();
		inner.method();
		System.out.println("[a="+inner.a+", b="+InnerClass.b+", c="+inner.c+"]");
	}
	
	public static void main(String[] args) {
		TestNest nest = new TestNest();
		nest.method2();
	}
}
