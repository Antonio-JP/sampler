package rioko.drawmodels.events;

import rioko.drawmodels.wizards.AbstractWizard;
import rioko.events.AbstractEvent;

public class WizardWindowEvent extends AbstractEvent {

	public static final int NONE = -1, OPEN_WIZARD = 0, CLOSE_WIZARD = 1;
	
	private int type = NONE;
	
	private AbstractWizard wizard = null;
	
	public WizardWindowEvent(int type, AbstractWizard wizard)
	{
		super();
		
		this.type = type;
		this.wizard = wizard;
		
		if(this.getClass().getSimpleName().equals(WizardWindowEvent.class.getSimpleName())) {
			this.processEvent();
		}
	}
	
	//Getters
	public int getType()
	{
		return this.type;
	}

	public Object getWizard() {
		return this.wizard;
	}
}
