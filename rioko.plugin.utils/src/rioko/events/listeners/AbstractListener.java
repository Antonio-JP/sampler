package rioko.events.listeners;

import org.eclipse.swt.widgets.Listener;

import rioko.events.AbstractEvent;

/**
 * Abstrac class representing a general listener of the custom events developed in this package.
 * 
 * It has information about the object which want to listen something.
 * 
 * @author Antonio
 *
 */
public abstract class AbstractListener implements Listener {

	/**
	 * Object interested in listen something
	 */
	protected Object parent = null;
	
	/**
	 * Static method to destroy a Listener. It calls the AbstractEvent#destroyListener method.
	 * 
	 * @param listener Listener to be disposed.
	 */
	public static void destroyListener(AbstractListener listener)
	{
		if(listener != null) {
			AbstractEvent.removeListener(listener);
			listener.dispose();
		}
	}
	
	/**
	 * Public builder to create a new Listener.
	 * 
	 * @param parent Object which want to listen something.
	 */
	public AbstractListener(Object parent)
	{
		this.parent = parent;
		
		//Añadimos el Listener a la lista de listeners que están escuchando eventos
		try {
			AbstractEvent.addListener(this);
		} catch (Exception e) {
			// Impossible exception
			e.printStackTrace();
		}
	}
	
	/**
	 * Abstract method required in all subclasses needed to destroy any Listener.
	 */
	protected abstract void dispose();
	
	/**
	 * Abstract method to get the required event class for a listener been run.
	 * 
	 * @return Class<?> with the class of the event.
	 */
	public abstract Class<? extends AbstractEvent> getAssociatedEvent();
}
