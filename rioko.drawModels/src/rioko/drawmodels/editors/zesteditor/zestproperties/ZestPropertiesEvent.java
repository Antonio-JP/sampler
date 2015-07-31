package rioko.drawmodels.editors.zesteditor.zestproperties;

import rioko.events.DataChangeEvent;

public class ZestPropertiesEvent extends DataChangeEvent {
	
	public final static int GENERIC = 1, ALGORITHM = 2, FILTERS = 4;
	
	private int change = 0;

	public ZestPropertiesEvent(ZestProperties data, int change) {
		super(data);
		
		this.change = change;
		
		this.processEvent();
	}
	
	public int getChange() {
		return this.change;
	}

}
