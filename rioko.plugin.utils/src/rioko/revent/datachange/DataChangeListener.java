package rioko.revent.datachange;

import rioko.revent.AbstractRListener;
import rioko.revent.BadArgumentForBuildingException;

public abstract class DataChangeListener extends AbstractRListener<DataChangeEvent> {

	private Object followed;
	
	public DataChangeListener(Object affected, Object followed) throws BadArgumentForBuildingException {
		super(affected, followed);
	}

	@Override
	public Class<? extends DataChangeEvent> getClassForListener() {
		return DataChangeEvent.class;
	}

	@Override
	public boolean checkedEvent(DataChangeEvent event) {
		return event.getChangedObject().equals(followed);
	}

	@Override
	protected void specificBuilder(Object... objects) throws BadArgumentForBuildingException {
		if(objects.length != 1) {
			throw new BadArgumentForBuildingException();
		} else {
			this.followed = objects[0];
		}
	}
	
}
