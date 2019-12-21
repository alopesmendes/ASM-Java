package RETRO_PROJECT.Features;

public class TryWithRessources implements Feature {

	@Override
	public boolean detectFeature(String feature) {
		return feature.equals("addSuppressed (Ljava/lang/Throwable;)V");
	}


}
