package rioko.savingviews.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import rioko.drawmodels.handlers.EditorDependentHandler;
import rioko.savingviews.FileFormatException;
import rioko.savingviews.ZestDisplay;

public class LoadingHandler  extends EditorDependentHandler{

	@Override
	protected Object run(ExecutionEvent event) throws ExecutionException {
		//Getting the selection (if exists) from Eclipse UI
		ISelection sel = HandlerUtil.getActiveMenuSelection(event);
		
		// Getting the default loading path
		IFile file = this.getFileAssociated();	
		IContainer folder = file.getParent();
		IFile dest = folder.getFile(new Path("./" + file.getName()+".smp"));
		
		//If we have a file selected, we put it as the file to load
		if(sel instanceof IStructuredSelection) {
			IStructuredSelection strSel = (IStructuredSelection)sel;
			
			Object obj = strSel.getFirstElement();
			
			if(obj instanceof IFile) {
				dest = (IFile) obj;
			}
		}
		
		// If the file given does not exist, we throw an error message and abort
		if(!dest.exists()) {
			MessageDialog.openError(null, "No display available", "The display you want to load does not exist.");
		} else {
			//Else, we load the display
			try {
				ZestDisplay display = new ZestDisplay(dest, this.getModel().getIdParser());
				
				this.getProperties().changeNestedAlgorithm(display.getAbstraction());
				this.getProperties().changeLayout(display.getLayout(this.getProperties().getActualLayout()));			
			} catch (FileFormatException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

}
