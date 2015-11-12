package rioko.drawmodels.views.listeners;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.events.WizardWindowEvent;
import rioko.drawmodels.events.listeners.WizardWindowListener;
import rioko.drawmodels.views.zestProperties.ZestPropertiesView;
import rioko.drawmodels.wizards.AbstractWizard;

public class PropertiesViewWizardListener extends WizardWindowListener {
	
	private ZestProperties properties;

	public PropertiesViewWizardListener(ZestPropertiesView parent) {
		super(AbstractWizard.class, parent);
	}

	@Override
	protected void openWizard(WizardWindowEvent wwe) { /* Do nothing */}

	@Override
	protected void closeWizard(WizardWindowEvent wwe) {
		((ZestPropertiesView)this.getAffectedObject()).updateView();
		properties.hasChanged(ZestEditor.UPDATE_ALL);
	}

	public void setProperties(ZestProperties properties) {
		this.properties = properties;
	}

}
