package rioko.drawmodels.handlers.extensions;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

import rioko.utilities.Log;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.filemanage.GeneralReader;
import rioko.drawmodels.filemanage.Reader;
import rioko.drawmodels.handlers.AbstractGenericHandler;
import rioko.drawmodels.wizards.SelectSpecialAlgorithmWizard;

public class OpenFile extends AbstractGenericHandler {

	@Override
	public Object execute(ExecutionEvent ee) throws ExecutionException {
		
		ISelection sel = HandlerUtil.getActiveMenuSelection(ee);
		if(sel instanceof IStructuredSelection) {
			IStructuredSelection strSel = (IStructuredSelection)sel;
			
			Object obj = strSel.getFirstElement();
			
			if(obj instanceof IFile) {
				IFile file = (IFile)obj;
				
				Reader<?> reader;
				try {
					Log.xOpen("wizard");
					Log.xPrint("Reading the model...");
					reader = GeneralReader.getReaderFromFile(file);
				
					ModelDiagram<?> model = reader.getModel();
					ZestEditor editor = new ZestEditor();
					
					Log.xPrint("Creating wizard...");
					SelectSpecialAlgorithmWizard wizard = new SelectSpecialAlgorithmWizard(editor, model.getModelDiagram());
					
					Log.xPrint("Running wizard...");
					wizard.init(HandlerUtil.getActiveWorkbenchWindow(ee).getWorkbench(), null);
					    
					// Instantiates the wizard container with the wizard and opens it
					WizardDialog dialog = new WizardDialog(null, wizard);
					
					dialog.create();
					dialog.open();
					
					Log.xPrint("Wizard opened. Waiting result...");
					int ret = dialog.getReturnCode();
					
					Log.xPrint("Return of Wizard -> " + ret);
					Log.xClose("wizard");
					
					ZestProperties properties = editor.getProperties().copy();
					editor.dispose();
					
					IEditorPart newEditor = IDE.openEditor(HandlerUtil.getActiveWorkbenchWindow(ee).getActivePage(), model/*new SpecialInputZestEditor(graph, properties, file.getName())*/, "rioko.drawmodels.editors.zestEditor");
					if(!(newEditor instanceof ZestEditor)) {
						throw new PartInitException("Bad editor created");
					}
					
					((ZestEditor)newEditor).changeZestProperties(properties);
					((ZestEditor)newEditor).updateView();
				} catch (IOException e) {
					Log.exception(e);
					MessageDialog.openError(null, "Error loading the File", 
							e.getMessage());
				} catch (PartInitException e) {
					Log.exception(e);
					MessageDialog.openError(null, "Error creating the Zest Editor", 
							e.getMessage());
				}
			}
		}
		
		return null;
	}

}
