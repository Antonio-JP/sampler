package rioko.revent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import rioko.utilities.collections.DeepStack;
import rioko.utilities.collections.HashList;

/**
 * Abstract class that represents and gives the basic functionality for an Event System
 *  
 * @author Antonio
 */
public abstract class REvent {
	/* Fields for a basic REvent */
	private Object source;
	
	/* Static objects for Event management */
	private static Map<Class<? extends REvent>, List<RListener<?>>> mapToListeners = new HashMap<>();
	private static List<RListener<?>> listOfListeners = new HashList<>();
	private static Queue<RListener<?>> auxToAdd = null;
	private static Queue<RListener<?>> auxToRemove = null;

	private static DeepStack<RListener<?>> stackOfAdd = new DeepStack<>();
	private static DeepStack<RListener<?>> stackOfRemove = new DeepStack<>();
	
	private static Boolean locked = false;
	
	/* Protected builder */
	protected REvent(Object source, Object ... objects) throws BadArgumentForBuildingException {
		this.source = source;
		
		this.specificBuilder(objects);
		
		this.throwEvent();
	}
	
	/* Public methods */
	
	/* Protected methods */
	protected Object getSource() {
		return this.source;
	}
	
	/* Private methods */
	private void throwEvent() {
		/* We block the mapToListeners variable */
		REvent.locked = true;
		
		REvent.pushQueues();
		
		/* We throw the event to its associated Listeners */
		for(Class<? extends REvent> clazz : mapToListeners.keySet()) {
			/* The class must inherit from the class of the event */
			if(clazz.isAssignableFrom(this.getClass())) {
			//if(this.getClass().isAssignableFrom(clazz)) {
				for(RListener<?> listener : mapToListeners.get(clazz)) {
					/* If someone want to remove a listener we skip it */
					if(!auxToRemove.contains(listener)) {	
						/* Then, we listen the event */
						listener.listenEvent(this);
					}
				}
			}
		}
		
		/* We made the event visible to stacked listeners */
		Iterator<RListener<?>> iterator = REvent.stackOfAdd.getDeepIterator();
		while(iterator.hasNext()) {
			iterator.next().listenEvent(this);
		}
		
		/* We update the mapToListeners */
		while(!auxToAdd.isEmpty()) {
			RListener<?> listener = auxToAdd.poll();
			/* We give the event for the new Listener */
			if(this.getClass().isAssignableFrom(listener.getClassForListener())) {
				if(!auxToRemove.contains(listener)) {
					listener.listenEvent(this);
				}
			}
			
			REvent.addListener(listener, false);
		}
		
		while(!auxToRemove.isEmpty()) {
			RListener<?> listener = auxToRemove.poll();
			REvent.removeListener(listener, false);
		}
		
		REvent.popQueues();
		
		/* We free the mapToListeners variable */
		REvent.locked = false;
	}

	/* Abstract methods */
	/**
	 * Method that must be implemented for the events that determines how they build.
	 * 
	 * @param objects Array of Objects to finish the built of the event.
	 */
	protected abstract void specificBuilder(Object ... objects) throws BadArgumentForBuildingException; 
	
	/* Static methods */
	private static void addListener(RListener<?> listener, Boolean check) {
		if(!REvent.listOfListeners.contains(listener)) {
			if(check && REvent.locked) {
				if((!REvent.auxToAdd.contains(listener)) && (!REvent.stackOfAdd.containsDeepElement(listener))) {
					REvent.auxToAdd.add(listener);
				}
			} else {
				Class<? extends REvent> classForListener = listener.getClassForListener();
				
				if(REvent.mapToListeners.get(classForListener) == null) {
					REvent.mapToListeners.put(classForListener, new ArrayList<>());
				}
				
				REvent.mapToListeners.get(classForListener).add(listener);
				REvent.listOfListeners.add(listener);
			}
		}
	}
	
	public static void addListener(RListener<?> listener) {
		REvent.addListener(listener, true);
	}
	
	private static void removeListener(RListener<?> listener, Boolean check) {
		Boolean isInside = REvent.listOfListeners.contains(listener);
		if(check && REvent.locked) {
			isInside |= REvent.auxToAdd.contains(listener);
			if(isInside) {
				REvent.auxToRemove.add(listener);
			}
		} else {
			if(isInside) {
				REvent.listOfListeners.remove(listener);
				REvent.mapToListeners.get(listener.getClassForListener()).remove(listener);
			} else if(REvent.listOfListeners.contains(listener)) {
				REvent.listOfListeners.remove(listener);
			}
		}
	}
	
	public static void removeListener(RListener<?> listener) {
		REvent.removeListener(listener, true);
	}
	
	private static void pushQueues() {
		if(auxToAdd != null) {
			stackOfAdd.push(auxToAdd);
		}
		
		if(auxToRemove != null) {
			stackOfRemove.push(auxToRemove);
		}
		
		auxToAdd = new LinkedList<>();
		auxToRemove = new LinkedList<>();
	}

	private static void popQueues() {
		if(!stackOfAdd.isEmpty()) {
			auxToAdd = (Queue<RListener<?>>) stackOfAdd.pop();
		} else {
			auxToAdd = null;
		}
		
		if(!stackOfRemove.isEmpty()) {
			auxToRemove = (Queue<RListener<?>>) stackOfRemove.pop();
		} else {
			auxToRemove = null;
		}
	}
}
