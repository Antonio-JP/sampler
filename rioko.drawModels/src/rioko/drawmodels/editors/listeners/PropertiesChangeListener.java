package rioko.drawmodels.editors.listeners;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.events.PropertiesChangeEvent;
import rioko.revent.datachange.DataChangeEvent;
import rioko.revent.datachange.DataChangeListener;

public class PropertiesChangeListener extends DataChangeListener {

	private ZestProperties properties;
	private ZestEditor editor;
	
	public PropertiesChangeListener(ZestProperties data, ZestEditor editor) throws Exception {
		super(editor, data);
		
		this.properties = data;
		this.editor = editor;
	}
	
	//Getters
	public ZestProperties getProperties()
	{
		return this.properties;
	}

	@Override
	public Class<? extends DataChangeEvent> getClassForListener() {
		return PropertiesChangeEvent.class;
	}

	@Override
	public void run(DataChangeEvent event) {
		this.editor.updateView(((PropertiesChangeEvent)event).getChanges());
	}

}
