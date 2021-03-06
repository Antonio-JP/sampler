package rioko.drawmodels.handlers.extensions;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import rioko.utilities.Log;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.filemanage.GeneralReader;
import rioko.drawmodels.filemanage.Reader;
import rioko.drawmodels.handlers.AbstractGenericHandler;

public class OpenFile extends AbstractGenericHandler {

	@Override
	public Object execute(ExecutionEvent ee) throws ExecutionException {
		ISelection sel = HandlerUtil.getActiveMenuSelection(ee);
		if(sel instanceof IStructuredSelection) {
			IStructuredSelection strSel = (IStructuredSelection)sel;
			
			Object obj = strSel.getFirstElement();
			
			if(obj instanceof IFile) {
				IFile file = (IFile)obj;
				
				try {
					this.openEditorWithFile(file, ee);
				} catch (PartInitException e) {
					Log.exception(e);
					MessageDialog.openError(null, "Error creating the Zest Editor", 
							e.getMessage());
				} catch (IOException e) {
					Log.exception(e);
					MessageDialog.openError(null, "Error loading the File", 
							e.getMessage());
				} catch (Exception e) {
					Log.exception(e);
					MessageDialog.openError(null, "Unexpected error loading the File", 
							e.getMessage());
				}
				
			}
		}
		
		return null;
	}
	
	public void openEditorWithFile(IFile file, ExecutionEvent ee) throws PartInitException, IOException, Exception {
		this.setContext(ee);
		this.openEditorWithFile(file);
	}
	
	protected void openEditorWithFile(IFile file) throws IOException, PartInitException, Exception {
		Reader<?> reader;
		Log.xOpen("wizard");
		Log.xPrint("Reading the model...");
		reader = GeneralReader.getReaderFromFile(file);
	
		ModelDiagram<?> model = reader.getModel();
		this.openZestEditor(model, file);
		Log.xClose("wizard");
	}

}
