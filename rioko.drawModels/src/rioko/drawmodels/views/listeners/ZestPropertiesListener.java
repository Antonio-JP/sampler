package rioko.drawmodels.views.listeners;

import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestPropertiesEvent;
import rioko.revent.datachange.DataChangeEvent;
import rioko.revent.datachange.DataChangeListener;

public abstract class ZestPropertiesListener extends DataChangeListener {

	public ZestPropertiesListener(ZestProperties data, Object parent) throws Exception {
		super(parent, data);
	}

	@Override
	public void run(DataChangeEvent event) {
		if(event instanceof ZestPropertiesEvent) {
			ZestPropertiesEvent zEvent = (ZestPropertiesEvent)event;
			if((zEvent.getChange() & ZestPropertiesEvent.GENERIC) != 0) {
				this.doWhenGenericChange();
			}
			
			if((zEvent.getChange() & ZestPropertiesEvent.ALGORITHM) != 0) {
				this.doWhenAlgorithmChange();
			}
			
			if((zEvent.getChange() & ZestPropertiesEvent.FILTERS) != 0) {
				this.doWhenFiltersChange();
			}
		}
	}

	protected abstract void doWhenGenericChange();
	protected abstract void doWhenAlgorithmChange();
	protected abstract void doWhenFiltersChange();
}
