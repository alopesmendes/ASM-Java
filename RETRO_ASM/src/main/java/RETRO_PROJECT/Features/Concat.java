package RETRO_PROJECT.Features;

public class Concat implements Feature {

	@Override
	public boolean detectFeature(String feature) {
		return feature.equals("makeConcatWithConstants");
	}

}
