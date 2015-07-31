package rioko.drawmodels.views.listeners;

import org.eclipse.swt.widgets.Event;

import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestPropertiesEvent;
import rioko.events.listeners.AbstractDataChangeListener;

public abstract class ZestPropertiesListener extends AbstractDataChangeListener {

	public ZestPropertiesListener(ZestProperties data, Object parent) throws Exception {
		super(data, parent);
	}

	@Override
	public void onDataChange(Event event) {
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
