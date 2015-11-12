package rioko.drawmodels.events;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.revent.BadArgumentForBuildingException;
import rioko.revent.datachange.DataChangeEvent;

public class PropertiesChangeEvent extends DataChangeEvent {

	
	private int changes = 0;
	
	public PropertiesChangeEvent(ZestProperties data) {
		this(data, ZestEditor.UPDATE_ALL);
	}
	
	public PropertiesChangeEvent(ZestProperties data, int changes)
	{
		super(data, changes);
	}

	//Getters
	public int getChanges()
	{
		return this.changes;
	}
	
	@Override
	protected void specificBuilder(Object ... objects) {
		if(objects.length != 1) {
			throw new BadArgumentForBuildingException();
		} else if(!(objects[0] instanceof Integer)) {
			throw new BadArgumentForBuildingException();
		} else {
			this.changes = (int) objects[0];
		}
	}
}
