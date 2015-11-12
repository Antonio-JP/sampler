package rioko.revent.datachange;

import rioko.revent.BadArgumentForBuildingException;
import rioko.revent.REvent;

public class DataChangeEvent extends REvent {
	
	public DataChangeEvent(Object source, Object ... objects) {
		super(source, objects);
	}
	
	public Object getChangedObject() {
		return this.getSource();
	}

	@Override
	protected void specificBuilder(Object... objects) { 
		if(objects.length > 0) {
			throw new BadArgumentForBuildingException();
		}
	}

}
