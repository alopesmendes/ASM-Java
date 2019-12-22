package fr.umlv.retro.features;

public class TryWithRessources implements Feature {

	@Override
	public boolean detectFeature(String feature) {
		return feature.equals("addSuppressed (Ljava/lang/Throwable;)V");
	}


}
