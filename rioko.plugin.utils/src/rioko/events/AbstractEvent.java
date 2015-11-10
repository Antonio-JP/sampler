package rioko.events;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Event;

import rioko.events.listeners.AbstractListener;

/**
 * Abstract Class implementing the basic and generic mechanism to handle different kind 
 * of customs events. It is specially usefull for events not registered in the API which 
 * is being used.
 * 
 * @author Antonio
 */
public abstract class AbstractEvent extends Event {

	/**
	 * Static map which associates the type of AbstractEvent to the listeners of that event
	 */
	protected static HashMap<Class<? extends AbstractEvent>, ArrayList<AbstractListener>> listeners = new HashMap<>();
	
	private static HashMap<Class<? extends AbstractEvent>, ArrayList<AbstractListener>> toAdd = new HashMap<>();
	private static HashMap<Class<? extends AbstractEvent>, ArrayList<AbstractListener>> toRemove = new HashMap<>();
	private static boolean blocked = false;
	
	/**
	 * Static method to register a listener to the system
	 * 
	 * @param listener AbstractListener to register
	 * 
	 * @throws Exception If the listener has no event associated
	 */
	public static void addListener(AbstractListener listener) throws Exception
	{
		if(listener.getAssociatedEvent() == null) {
			throw new Exception("Rioko ERROR: no event associated with an event");
		}
		
		HashMap<Class<? extends AbstractEvent>, ArrayList<AbstractListener>> where = listeners;
		if(blocked) {
			where = toAdd;
		}
		if(where.get(listener.getAssociatedEvent()) == null) {
			where.put(listener.getAssociatedEvent(), new ArrayList<AbstractListener>());
		}
		
		where.get(listener.getAssociatedEvent()).add(listener);
	}
	
	/**
	 * Static method to dispose a listener of the system.
	 * 
	 * @param listener AbstractListener to dispose
	 * 
	 * @return true if the listener was previously registered and false otherwise
	 */
	public static boolean removeListener(AbstractListener listener)
	{
		if(blocked) {
			if(toRemove.get(listener.getAssociatedEvent()) == null) {
				toRemove.put(listener.getAssociatedEvent(), new ArrayList<AbstractListener>());
			}
			
			toRemove.get(listener.getAssociatedEvent()).add(listener);
			
			return true;
		} else {
			ArrayList<AbstractListener> list = listeners.get(listener.getAssociatedEvent());
			if(list != null) {
				return list.remove(listener);
			}
		}
		
		return false;
	}
	
	/**
	 * Method to handle a new event. It is protected to let freedom to change in subclasses. 
	 * This default implementation execute the handleEvent method of the AbstractListener 
	 * class associated to all the listener of this event.
	 */
	protected void processEvent()
	{
		//Block the HashMap of Listeners to avoid Concurrent Modifications
		blocked = true;
		//Create new toAdd and toRemove maps
		toAdd = new HashMap<>();
		toRemove = new HashMap<>();
		
		//Execute the listeners "handleEvents"
		for(Class<? extends AbstractEvent> event : listeners.keySet()) {
			if(event.isInstance(this)) {
				for(AbstractListener listener : listeners.get(event)) {
						listener.handleEvent(this);
				}
			}
		}
		
		//Make the changes in the HashMap
		for(Class<? extends AbstractEvent> event : toAdd.keySet()) {
			if(listeners.get(event) == null) {
				listeners.put(event, new ArrayList<AbstractListener>());
			}
			
			for(AbstractListener listener : toAdd.get(event)) {
				listeners.get(event).add(listener);
			}
		}
		
		for(Class<? extends AbstractEvent> event : toRemove.keySet()) {
			if(listeners.get(event) != null) {
				for(AbstractListener listener : toRemove.get(event)) {
					listeners.get(event).remove(listener);
				}
			}
		}
		
		//Unlock the tables
		blocked = false;
	}
}
