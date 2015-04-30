package rioko.drawmodels.events.listeners;

import org.eclipse.swt.widgets.Event;

import rioko.drawmodels.events.WizardWindowEvent;
import rioko.drawmodels.wizards.AbstractWizard;
import rioko.events.AbstractEvent;
import rioko.events.listeners.AbstractListener;

public abstract class WizardWindowListener extends AbstractListener {

	private Class<? extends AbstractWizard> typeOfWizard;
	
	public WizardWindowListener(Class<? extends AbstractWizard> typeOfWizard, Object parent) {
		super(parent);
		
		this.typeOfWizard = typeOfWizard;
	}
	
	@Override
	public void handleEvent(Event e) {
		if(e instanceof WizardWindowEvent) {
			WizardWindowEvent wwe = (WizardWindowEvent)e;
			
			if(this.typeOfWizard.isInstance(wwe.getWizard())) {
				if(wwe.getType() == WizardWindowEvent.CLOSE_WIZARD) {
					this.closeWizard(wwe);
				} else if(wwe.getType() == WizardWindowEvent.OPEN_WIZARD) {
					this.openWizard(wwe);
				}
			}
		}
	}
	
	@Override
	public Class<? extends AbstractEvent> getAssociatedEvent() {
		return WizardWindowEvent.class;
	}
	
	@Override
	protected void dispose() { /* Do nothing */	}

	//Abstract methods
	protected abstract void openWizard(WizardWindowEvent wwe);
	protected abstract void closeWizard(WizardWindowEvent wwe);
}
