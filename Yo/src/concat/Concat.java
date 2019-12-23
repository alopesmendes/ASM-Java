package concat;

public class Concat {
	
	private int i;
	private String s = "aaa"+"bbb"+i;
	
	public static void main(String[] args) {
		Concat concat = new Concat();
		String c = args[1]+"var"+" "+args[0];
		System.out.println(c+concat.s);
	}
}
