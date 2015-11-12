package rioko.drawmodels.events;

import rioko.drawmodels.wizards.AbstractWizard;
import rioko.revent.BadArgumentForBuildingException;
import rioko.revent.REvent;

public class WizardWindowEvent extends REvent {

	public static final int NONE = -1, OPEN_WIZARD = 0, CLOSE_WIZARD = 1;
	
	private int type = NONE;
	
	private AbstractWizard wizard = null;
	
	public WizardWindowEvent(int type, AbstractWizard wizard)
	{
		super(wizard, type);
		
		this.type = type;
		this.wizard = wizard;
	}
	
	//Getters
	public int getType()
	{
		return this.type;
	}

	public Object getWizard() {
		return this.wizard;
	}

	@Override
	protected void specificBuilder(Object... objects) throws BadArgumentForBuildingException {
		try {
			this.wizard = (AbstractWizard)this.getSource();
			this.type = (int) objects[0];
		} catch(Exception e) {
			throw new BadArgumentForBuildingException();
		}
	}
}
