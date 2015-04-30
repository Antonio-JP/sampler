package rioko.drawmodels.handlers.algorithms;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
//import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.AbstractGenericHandler;
import rioko.drawmodels.wizards.SelectSpecialAlgorithmWizard;

public class SelectAlgorithmWizardHandler extends AbstractGenericHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		try{
			IWorkbenchPage page = this.getEditorPage();
		
			IEditorPart editor = page.getActiveEditor();
			
			if(editor instanceof ZestEditor) {
//				MessageDialog.openInformation(null, 
//						"¡Error!", "Action not yet implemented");
				SelectSpecialAlgorithmWizard wizard = new SelectSpecialAlgorithmWizard((ZestEditor) editor);
				
				wizard.init(editor.getSite().getWorkbenchWindow().getWorkbench(), null);
				    
				// Instantiates the wizard container with the wizard and opens it
				WizardDialog dialog = new WizardDialog(editor.getSite().getWorkbenchWindow().getShell(), wizard);
				
				dialog.create();
				dialog.open();
				
				dialog.getReturnCode();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
