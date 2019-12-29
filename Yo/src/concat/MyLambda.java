package concat;

import java.util.function.IntUnaryOperator;

public class MyLambda {
	private static int mod(int i) {
		return i%2;
	}
	
	public static int dev(int i) {
		return i/2;
	}
	
	public void mod2() {
		IntUnaryOperator iop = MyLambda::mod;
		IntUnaryOperator op2 = MyLambda::dev;
		System.out.println(iop.applyAsInt(5) + " " + op2.applyAsInt(5));
	}
}
