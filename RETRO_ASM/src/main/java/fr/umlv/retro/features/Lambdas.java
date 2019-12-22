package fr.umlv.retro.features;

public class Lambdas implements Feature {

	@Override
	public boolean detectFeature(String feature) {
		return feature.equals("java/lang/invoke/LambdaMetafactory");
	}


}
