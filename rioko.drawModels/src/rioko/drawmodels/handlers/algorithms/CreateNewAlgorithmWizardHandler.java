package rioko.drawmodels.handlers.algorithms;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.wizard.WizardDialog;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.EditorDependentHandler;
import rioko.drawmodels.wizards.CreateSpecialAlgorithmWizard;

public class CreateNewAlgorithmWizardHandler extends EditorDependentHandler {

	@Override
	public Object run(ExecutionEvent arg0) throws ExecutionException {
		
		
		InputDialog inDialog = new InputDialog(null, "Select Algorithm Title", "Put here the name of the algorithm you want to create", "", new NameValidator());
		inDialog.open();
				
		String name = inDialog.getValue();
		if(name != null && !name.equals("")) {
			CreateSpecialAlgorithmWizard wizard = new CreateSpecialAlgorithmWizard((ZestEditor) this.editor, inDialog.getValue());
			
			wizard.init(this.editor.getSite().getWorkbenchWindow().getWorkbench(), null);
			    
			// Instantiates the wizard container with the wizard and opens it
			WizardDialog dialog = new WizardDialog(null, wizard);
			
			dialog.create();
			dialog.open();
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
