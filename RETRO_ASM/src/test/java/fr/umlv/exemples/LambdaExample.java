package fr.umlv.exemples;

import java.util.ArrayList;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LambdaExample {
	private final int a;
	
	public LambdaExample(int a) {
		super();
		this.a = a;
	}

	public static void main(String[] args) {
		var test = new LambdaExample(3);
		var list = new ArrayList<String>();
		list.forEach(System.out::println);
		list.forEach(x-> System.out.println(x));
		
		
		
		BiFunction<Integer, Integer, Long> additionner = (val1, val2) -> (long) val1 + val2;
		Function<Integer, Boolean> isPositif = valeur -> {
	          if (valeur >= 0) {
	              return true;
	          }
	          return false;
	    };
	    
	    if (isPositif.apply(test.a)) {
	    	System.out.println(additionner.apply(1,test.a));
	    }
		
		
	}

}
