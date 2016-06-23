package rioko.sampler.directoryDrawer.handler;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.filemanage.Reader;
import rioko.drawmodels.handlers.AbstractGenericHandler;
import rioko.sampler.directoryDrawer.reader.FolderReader;
import rioko.utilities.Log;

public class OpenDirectoryVisualization extends AbstractGenericHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		if(sel instanceof IStructuredSelection) {
			IStructuredSelection strSel = (IStructuredSelection)sel;
			
			Object obj = strSel.getFirstElement();
			
			if(obj instanceof IResource) {
				try {
					this.openEditorWithFile((IResource)obj, event);
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
	
	public void openEditorWithFile(IResource file, ExecutionEvent ee) throws PartInitException, IOException, Exception {
		this.setContext(ee);
		this.openEditorWithFile(file);
	}
	
	protected void openEditorWithFile(IResource resource) throws IOException, PartInitException, Exception {
		Log.xOpen("wizard");
		Log.xPrint("Reading the model...");
		Reader<?> reader = new FolderReader(resource);
	
		ModelDiagram<?> model = reader.getModel();
		this.openZestEditor(model, resource);
		Log.xClose("wizard");
	}

}
