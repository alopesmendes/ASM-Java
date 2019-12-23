/**
 * 
 */
package fr.umlv.retro.observer;
import fr.umlv.retro.features.Feature;

/**
 * @author lopes
 *
 */
public interface Observer {
	void onMessageReceived(Class<? extends Feature> type, String msg);
}
