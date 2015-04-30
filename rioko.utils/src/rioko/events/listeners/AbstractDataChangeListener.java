package rioko.events.listeners;

import org.eclipse.swt.widgets.Event;

import rioko.events.AbstractEvent;
import rioko.events.DataChangeEvent;

/**
 * Abstract class to listen a DataChangeEvent. It has the information of which Object is being followed and 
 * @author Antonio
 *
 */
public abstract class AbstractDataChangeListener extends AbstractListener {
	
	/**
	 * Private field containing the object followed by this Listener
	 */
	private Object dataToFollow = null;
	
	/**
	 * Public builder for a Listener specifying the object followed and the object which is interested in listen.
	 * 
	 * @param data Data to be followed
	 * @param parent Object which want to follow data
	 * @throws Exception If there is a null parameter
	 */
	public AbstractDataChangeListener(Object data, Object parent) throws Exception
	{
		super(parent);
		
		if(data == null) {
			throw new Exception("Rioko ERROR: no data to follow");
		}
		
		if(parent == null) {
			throw new Exception("Rioko ERROR: no parent to sent the event");
		}
		
		this.dataToFollow = data;
	}

	@Override
	public void handleEvent(Event event) {
		if(event instanceof DataChangeEvent) {
			DataChangeEvent dtEvent = (DataChangeEvent)event;
			
			if(this.dataToFollow == dtEvent.getData())
			{
				this.onDataChange(event);
			}
		}
	}

	@Override
	public Class<? extends AbstractEvent> getAssociatedEvent() {
		return DataChangeEvent.class;
	}
	
	//Abstract methods
	/**
	 * Abstract methods require in any extension of this class in which each Listener class defines the code to be execute when the listener catch an associated event.
	 * 
	 * @param event Event which was handled.
	 */
	public abstract void onDataChange(Event event);
}
