package rioko.drawmodels.views.listeners;

import org.eclipse.swt.widgets.Event;

import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.views.zestProperties.ZestPropertiesView;
import rioko.events.listeners.AbstractDataChangeListener;

public class PropertiesChangeListener extends AbstractDataChangeListener {

	private ZestProperties properties;
	private ZestPropertiesView view;
	
	public PropertiesChangeListener(ZestProperties data, ZestPropertiesView view) throws Exception {
		super(data, view);
		
		this.properties = data;
		this.view = view;
	}
	
	//Getters
	public ZestProperties getProperties()
	{
		return this.properties;
	}

	@Override
	public void onDataChange(Event event) {
		this.view.updateView();
	}
	
	//Other methods
	@Override
	protected void dispose() {}
	
}
