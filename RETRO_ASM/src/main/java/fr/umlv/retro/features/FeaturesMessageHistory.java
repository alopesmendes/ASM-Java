package fr.umlv.retro.features;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FeaturesMessageHistory implements Observer {
	private final ArrayList<String> messages = new ArrayList<>();

	@Override
	public void onMessageReceived(String msg) {
		messages.add(msg);
	}
	
	@Override
	public String toString() {
		return 	messages.stream().collect(Collectors.joining("\n"));
	}
}
