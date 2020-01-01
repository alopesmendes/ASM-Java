/**
 * 
 */
package fr.umlv.retro.observer;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import fr.umlv.retro.features.Feature;

/**
 * @author lopes
 *
 */
public class ObserverHistory implements Observer {
	private final HashSet<Class<? extends Feature>> set = new HashSet<>();
	private final ClassValue<Supplier<HashSet<String>>> values = new ClassValue<>() {
		@Override
		protected Supplier<HashSet<String>> computeValue(Class<?> type) {
			HashSet<String> messages = new HashSet<>();
			return () -> messages;
		}
	};

	@Override
	public void onMessageReceived(Class<? extends Feature> type, String msg) {
		Objects.requireNonNull(msg);
		Objects.requireNonNull(type);
		set.add(type);
		values.get(type).get().add(msg);
	}
	
	/**
	 * @param typeFeature
	 * @return all the information of a type.
	 */
	public HashSet<String> infoOf(Class<? extends Feature> typeFeature) {
		return values.get(typeFeature).get();
	}
	
	@Override
	public String toString() {
		return 	set.stream().map(e -> values.get(e).get()).
				flatMap(l -> l.stream()).collect(Collectors.joining("\n"));
	}
	
	
}
