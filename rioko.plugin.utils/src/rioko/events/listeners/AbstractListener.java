package rioko.events.listeners;

import org.eclipse.swt.widgets.Listener;

import rioko.events.AbstractEvent;

/**
 * Abstract class representing a general listener of the custom events developed in this package.
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
	 * Static method to destroy an AbstractListener. It calls the {@link rioko.events.AbstractEvent#removeListener removeListener} method.
	 * 
	 * @param listener AbstractListener to be disposed.
	 */
	public static void destroyListener(AbstractListener listener)
	{
		if(listener != null) {
			AbstractEvent.removeListener(listener);
			listener.dispose();
		}
	}
	
	/**
	 * Public builder to create a new AbstractListener.
	 * 
	 * @param parent Object which want to follow.
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
	 * @return The class of the event (extending {@link rioko.events.AbstractEvent}).
	 */
	public abstract Class<? extends AbstractEvent> getAssociatedEvent();
}
