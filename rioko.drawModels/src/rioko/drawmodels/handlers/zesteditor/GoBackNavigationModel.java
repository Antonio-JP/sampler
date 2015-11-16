package rioko.drawmodels.handlers.zesteditor;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.EditorDependentHandler;

public class GoBackNavigationModel extends EditorDependentHandler {

	@Override
	public Object run(ExecutionEvent arg0) throws ExecutionException {
		((ZestEditor)editor).getProperties().popConfiguration();
		
		return null;
	}

}
