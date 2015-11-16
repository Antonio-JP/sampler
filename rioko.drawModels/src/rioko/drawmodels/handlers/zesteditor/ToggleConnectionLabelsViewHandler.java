package rioko.drawmodels.handlers.zesteditor;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.EditorDependentHandler;

public class ToggleConnectionLabelsViewHandler extends EditorDependentHandler {

	@Override
	public Object run(ExecutionEvent arg0) throws ExecutionException {
		((ZestEditor)editor).toggleShowingConnectionLabels();
		
		return null;
	}
}
