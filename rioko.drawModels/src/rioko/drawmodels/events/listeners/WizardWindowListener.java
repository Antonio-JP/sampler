package rioko.drawmodels.events.listeners;

import rioko.drawmodels.events.WizardWindowEvent;
import rioko.drawmodels.wizards.AbstractWizard;
import rioko.revent.AbstractRListener;
import rioko.revent.BadArgumentForBuildingException;

public abstract class WizardWindowListener extends AbstractRListener<WizardWindowEvent> {

	private Class<? extends AbstractWizard> typeOfWizard;
	
	public WizardWindowListener(Class<? extends AbstractWizard> typeOfWizard, Object parent) {
		super(parent, typeOfWizard);
		
		this.typeOfWizard = typeOfWizard;
	}
	
	@Override
	public void run(WizardWindowEvent wwe) {
		if(wwe.getType() == WizardWindowEvent.CLOSE_WIZARD) {
			this.closeWizard(wwe);
		} else if(wwe.getType() == WizardWindowEvent.OPEN_WIZARD) {
			this.openWizard(wwe);
		}
	}
	
	@Override
	public Class<? extends WizardWindowEvent> getClassForListener() {
		return WizardWindowEvent.class;
	}

	@Override
	public boolean checkedEvent(WizardWindowEvent event) {
		return this.typeOfWizard.isInstance(event.getWizard());
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void specificBuilder(Object... objects) throws BadArgumentForBuildingException {
		try {
			this.typeOfWizard = (Class<? extends AbstractWizard>) objects[0];
		} catch (Exception e) {
			throw new BadArgumentForBuildingException();
		}
	}
	

	//Abstract methods
	protected abstract void openWizard(WizardWindowEvent wwe);
	protected abstract void closeWizard(WizardWindowEvent wwe);

	
}
