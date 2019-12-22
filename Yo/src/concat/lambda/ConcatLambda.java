package concat.lambda;

import java.util.function.UnaryOperator;

public class ConcatLambda {
	public static void main(String[] args) {
		UnaryOperator<String> op = (s) -> s + " " + args[0];
		System.out.println(op.apply("lambda"));
	}
}
