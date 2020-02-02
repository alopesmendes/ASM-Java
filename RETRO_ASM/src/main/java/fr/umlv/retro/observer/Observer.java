/**
 * 
 */
package fr.umlv.retro.observer;
import fr.umlv.retro.features.Feature;

/**
 * The Observer interface will receive messages destined to a certain type of feature.
 * @author lopes mendes
 * @author lambert-delavalquerie
 */
public interface Observer {
	/**
	 * Receives a certain message address to a certain type.
	 * @param type to determine which feature.
	 * @param msg from a feature.
	 */
	void onMessageReceived(Class<? extends Feature> type, String msg);
}
