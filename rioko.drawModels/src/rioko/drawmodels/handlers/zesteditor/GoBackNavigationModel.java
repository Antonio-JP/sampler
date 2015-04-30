package rioko.drawmodels.handlers.zesteditor;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.AbstractGenericHandler;

public class GoBackNavigationModel extends AbstractGenericHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		try{
			IWorkbenchPage page = this.getEditorPage();
		
			IEditorPart editor = page.getActiveEditor();
			
			if(editor instanceof ZestEditor) {
				((ZestEditor)editor).getProperties().popConfiguration();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
