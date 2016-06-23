package rioko.drawmodels.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;

public abstract class EditorDependentHandler extends AbstractGenericHandler {

	protected IEditorPart editor;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try{
			IWorkbenchPage page = this.getEditorPage();
		
			this.editor = page.getActiveEditor();
			
			if(this.editor instanceof ZestEditor) {
				return this.run(event);
			} else {
				MessageDialog.openError(null, "Zest Editor Required", "For this actions it is necessary have openend a Zest Editor");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/* Protected abstract methods */
	protected abstract Object run(ExecutionEvent event) throws ExecutionException;
	
	/* Protected implemented methods */
	protected ModelDiagram<?> getModel() {
		if(this.editor != null) {
			return ((ZestEditor)this.editor).getModel();
		}
		
		return null;
	}

	protected ZestProperties getProperties() {
		if(this.editor != null) {
			return ((ZestEditor)this.editor).getProperties();
		}
		
		return null;
	}

	protected IResource getFileAssociated() {
		if(this.editor != null) {
			return ((ZestEditor)this.editor).getFile();
		}
		
		return null;
	}
}
