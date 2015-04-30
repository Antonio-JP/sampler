package rioko.drawmodels.events;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.events.DataChangeEvent;

public class PropertiesChangeEvent extends DataChangeEvent {

	
	private int changes = 0;
	
	public PropertiesChangeEvent(ZestProperties data) {
		this(data, ZestEditor.UPDATE_ALL);
	}
	
	public PropertiesChangeEvent(ZestProperties data, int changes)
	{
		super(data);
		
		this.changes = changes;
		
		this.processEvent();
	}

	//Getters
	public int getChanges()
	{
		return this.changes;
	}
}
