package concat;

public class Concat {
	
	String m(String a, int b) {
		return a + "(" + b + ")"+42;
	}
	
	/*public static void main(String[] args) {
		int a = 5+2;
		System.out.println(a);
		String c = a+"var";
		String d = c+"ca"+c+c+c+c+"3";
		System.out.println(d);
	}*/
	
	public static void main(String[] args) {
		Concat c = new Concat();
		System.out.println(c.m("ici", 5));
	}
}
