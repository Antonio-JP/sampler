package rioko.drawmodels.editors.listeners;

import org.eclipse.swt.widgets.Event;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.events.PropertiesChangeEvent;
import rioko.events.AbstractEvent;
import rioko.events.listeners.AbstractDataChangeListener;

public class PropertiesChangeListener extends AbstractDataChangeListener {

	private ZestProperties properties;
	private ZestEditor editor;
	
	public PropertiesChangeListener(ZestProperties data, ZestEditor editor) throws Exception {
		super(data, editor);
		
		this.properties = data;
		this.editor = editor;
	}
	
	//Getters
	public ZestProperties getProperties()
	{
		return this.properties;
	}
	
	@Override
	public void handleEvent(Event event)
	{
		if(event instanceof PropertiesChangeEvent)
		{
			super.handleEvent(event);
		}
	}

	@Override
	public void onDataChange(Event event) {
		this.editor.updateView(((PropertiesChangeEvent)event).getChanges());
	}

	@Override
	protected void dispose() {	}

	@Override
	public Class<? extends AbstractEvent> getAssociatedEvent() {
		return PropertiesChangeEvent.class;
	}

}
