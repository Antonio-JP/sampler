package rioko.drawmodels.views.listeners;

import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.views.zestProperties.ZestPropertiesView;
import rioko.revent.BadArgumentForBuildingException;
import rioko.revent.datachange.DataChangeEvent;
import rioko.revent.datachange.DataChangeListener;

public class PropertiesChangeListener extends DataChangeListener {

	private ZestProperties properties;
	private ZestPropertiesView view;
	
	public PropertiesChangeListener(ZestProperties data, ZestPropertiesView view) throws Exception {
		super(view, data);
	}
	
	//Getters
	public ZestProperties getProperties()
	{
		return this.properties;
	}

	@Override
	public void run(DataChangeEvent event) {
		this.view.updateView();
	}
	
	@Override
	public void specificBuilder(Object ... objects) {
		try {
			this.properties = (ZestProperties)objects[0];
			this.view = (ZestPropertiesView)this.getAffectedObject(); 
		} catch(Exception e) {
			throw new BadArgumentForBuildingException();
		}
	}
	
}
