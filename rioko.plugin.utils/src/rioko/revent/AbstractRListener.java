package rioko.revent;

public abstract class AbstractRListener<E extends REvent> implements RListener<E> {
	
	private Object affected;
	
	/* Builders */
	protected AbstractRListener(Object affected, Object ... objects) throws BadArgumentForBuildingException{
		this.affected = affected;
		
		this.specificBuilder(objects);
		
		REvent.addListener(this);
	}
	
	/* Protected methods */
	protected Object getAffectedObject() {
		return this.affected;
	}
	
	/* Abstract methods */
	protected abstract void specificBuilder(Object ... objects) throws BadArgumentForBuildingException;
}
