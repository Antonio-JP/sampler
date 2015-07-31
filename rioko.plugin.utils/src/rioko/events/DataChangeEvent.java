package rioko.events;

/**
 * Event created to manage the changes in a class. It has the information of 
 * the object changed, so it is possible to control if a listener has to be 
 * execute or not.
 * 
 * @author Antonio
 */
public class DataChangeEvent extends AbstractEvent {
	
	/**
	 * Protected field in which save the object that has been changed.
	 */
	protected Object data;	
	
	/**
	 * Public builder to create a new Event associated with a data object.
	 * 
	 * @param data Object that has changed.
	 */
	public DataChangeEvent(Object data)
	{
		super();
		
		this.data = data;
		
		/* If this event is a real DataChangeEvent, we execute the event. Otherwise, 
		 * this is a subclass of this and it could be not constructed yet. */
		if(this.getClass().getSimpleName().equals(DataChangeEvent.class.getSimpleName())) {
			this.processEvent();
		}
	}
	
	//Getters
	/**
	 * Public method to get the data Object
	 * 
	 * @return The Object data
	 */
	public Object getData()
	{
		return this.data;
	}
}
