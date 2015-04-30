package rioko.drawmodels.wizards.pages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;

public abstract class CustomWizardPage extends WizardPage {

	public CustomWizardPage(String pageName) {
		super(pageName);
	}

	public CustomWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	public abstract boolean isWizardFinishable();
}
