package fr.umlv.retro.features;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;


public class FeaturesHistory {
	private final HashSet<Feature> set = new HashSet<>();
	private final ArrayList<Feature> features = new ArrayList<>();
	
	public boolean add(Feature feature) {
		Objects.requireNonNull(feature);
		return set.add(feature) && features.add(feature);
	}
	
	public Feature getLast() {
		return features.get(features.size() - 1);
	}
	
	public Feature get(int index) {
		if (index < 0) {
			throw new IndexOutOfBoundsException("index < 0");
		}
		if (index >= features.size()) {
			throw new IndexOutOfBoundsException("index >= size");
		}
		return features.get(index);
	}
}
