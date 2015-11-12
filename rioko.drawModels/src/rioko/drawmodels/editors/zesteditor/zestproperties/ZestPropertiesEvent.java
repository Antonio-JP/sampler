package rioko.drawmodels.editors.zesteditor.zestproperties;

import rioko.revent.BadArgumentForBuildingException;
import rioko.revent.datachange.DataChangeEvent;

public class ZestPropertiesEvent extends DataChangeEvent {
	
	public final static int GENERIC = 1, ALGORITHM = 2, FILTERS = 4;
	
	private int change = 0;

	public ZestPropertiesEvent(ZestProperties data, int change) {
		super(data, (Integer)change);
	}
	
	public int getChange() {
		return this.change;
	}
	
	@Override
	protected void specificBuilder(Object ... objects) {
		if(objects.length != 1) {
			throw new BadArgumentForBuildingException();
		} else if(!(objects[0] instanceof Integer)) {
			throw new BadArgumentForBuildingException();
		} else {
			this.change = (int) objects[0];
		}
	}

}
