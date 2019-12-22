package fr.umlv.retro.features;

public class Concat implements Feature {

	@Override
	public boolean detectFeature(String feature) {
		return feature.equals("makeConcatWithConstants");
	}

}
