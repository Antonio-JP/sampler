package rioko.drawmodels.wizards;

import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;

import rioko.drawmodels.events.WizardWindowEvent;
import rioko.drawmodels.wizards.pages.CustomWizardPage;

public abstract class AbstractWizard extends Wizard {
	
	IWorkbench workbench;
	IStructuredSelection selection;

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	} 
	
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		this.updateWizardData(page);
		
		return super.getNextPage(page);
	}
	
	@Override
	public IWizardPage getPage(String name) {
		this.updateWizardData(this.getCurrentPage());
		
		return super.getPage(name);
	}
	
	@Override 
	public IWizardPage getPreviousPage(IWizardPage page) {
		this.updateWizardData(page);
		
		return super.getPreviousPage(page);
	}

	@Override
	public boolean canFinish()
	{
		IWizardPage current = this.getCurrentPage();
		
		if(!(current instanceof CustomWizardPage)) {
			return false;
		} else {
			return ((CustomWizardPage)current).isWizardFinishable();
		}
	}
	
	@Override
	public boolean performFinish()
	{
		new WizardWindowEvent(WizardWindowEvent.CLOSE_WIZARD, this);
		
		return true;
	}
	
	//Private methods
	protected IWizardPage[] getPagesOfType(Class<? extends CustomWizardPage> pageClass) {
		ArrayList<CustomWizardPage> list = new ArrayList<>();
		
		for(IWizardPage page : this.getPages()) {
			if(pageClass.isInstance(page)) {
				list.add((CustomWizardPage) page);
			}
		}
		
		return list.toArray(new IWizardPage[0]);
	}
	
	protected IWizardPage getCurrentPage()
	{
		return this.getContainer().getCurrentPage();
	}
	

	protected abstract void updateWizardData(IWizardPage page);
}
