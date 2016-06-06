package rioko.drawmodels.handlers.algorithms;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.wizard.WizardDialog;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.EditorDependentHandler;
import rioko.drawmodels.wizards.SelectSpecialAlgorithmWizard;

public class SelectAlgorithmWizardHandler extends EditorDependentHandler {

	@Override
	public Object run(ExecutionEvent arg0) throws ExecutionException {
		SelectSpecialAlgorithmWizard wizard = new SelectSpecialAlgorithmWizard((ZestEditor) editor);
				
		wizard.init(this.editor.getSite().getWorkbenchWindow().getWorkbench(), null);
				    
		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(this.editor.getSite().getWorkbenchWindow().getShell(), wizard);
			
		dialog.create();
		dialog.open();
				
		dialog.getReturnCode();
		
		return null;
	}

}
