package rioko.drawmodels.handlers.zesteditor;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;

import rioko.drawmodels.dialogs.SearchDialog;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.EditorDependentHandler;
import rioko.graphabstraction.diagram.DiagramNode;

public class SearchNodes extends EditorDependentHandler {

	@Override
	public Object run(ExecutionEvent arg0) throws ExecutionException {
		SearchDialog dialog = new SearchDialog(null, ((ZestEditor)editor).getModel());
				
		if(dialog.open() == Window.OK) {
			DiagramNode searchNode = dialog.searched();
		
			((ZestEditor)editor).getProperties().setRootNode(searchNode);
		}
	
		return null;
	}

}
