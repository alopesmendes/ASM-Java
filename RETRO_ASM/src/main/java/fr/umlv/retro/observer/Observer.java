/**
 * 
 */
package fr.umlv.retro.observer;
import fr.umlv.retro.features.Feature;

public interface Observer {
	/**
	 * Receives a certain message address to a certain type.
	 * @param type
	 * @param msg
	 */
	void onMessageReceived(Class<? extends Feature> type, String msg);
}
