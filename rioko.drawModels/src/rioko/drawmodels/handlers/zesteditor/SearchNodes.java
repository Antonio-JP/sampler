package rioko.drawmodels.handlers.zesteditor;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import rioko.drawmodels.dialogs.SearchDialog;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.AbstractGenericHandler;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.utilities.Log;

public class SearchNodes extends AbstractGenericHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		try {
			if(this.getEditorPage().getActiveEditor() instanceof ZestEditor) {
				ZestEditor editor = (ZestEditor)this.getEditorPage().getActiveEditor();
				
				SearchDialog dialog = new SearchDialog(null, editor.getModel());
				
				if(dialog.open() == Window.OK) {
					DiagramNode searchNode = dialog.searched();
				
					editor.getProperties().setRootNode(searchNode);
				}
			}
		} catch (Exception e) {
			Log.exception(e);
			Log.print("Exception of type " + e.getClass().getSimpleName() + " catched");
		}
		return null;
	}

}
