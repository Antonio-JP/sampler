package rioko.drawmodels.handlers.algorithms;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.AbstractGenericHandler;
import rioko.drawmodels.wizards.CreateSpecialAlgorithmWizard;

public class CreateNewAlgorithmWizardHandler extends AbstractGenericHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		try{
			IWorkbenchPage page = this.getEditorPage();
		
			IEditorPart editor = page.getActiveEditor();
			
			if(editor instanceof ZestEditor) {

				InputDialog inDialog = new InputDialog(null, "Select Algorithm Title", "Put here the name of the algorithm you want to create", "", new NameValidator());
				inDialog.open();
				
				String name = inDialog.getValue();
				if(name != null && !name.equals("")) {
					CreateSpecialAlgorithmWizard wizard = new CreateSpecialAlgorithmWizard((ZestEditor) editor, inDialog.getValue());
					
					wizard.init(editor.getSite().getWorkbenchWindow().getWorkbench(), null);
					    
					// Instantiates the wizard container with the wizard and opens it
					WizardDialog dialog = new WizardDialog(null, wizard);
					
					dialog.create();
					dialog.open();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	private class NameValidator implements IInputValidator {

		@Override
		public String isValid(String newText) {
			if (newText.length() > 0) {
				return null;
			} else {
				return "The name of the algorithm should have at least 1 charachter";
			}
		}
		
	}
}
