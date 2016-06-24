package rioko.sampler.emf_splitter.directorydrawer;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.mondo.generate.modular.project.ext.ModularHandler;

import rioko.sampler.directoryDrawer.handler.OpenDirectoryVisualization;

public class SamplerDirectoryOpenWithHandler extends ModularHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			ISelectionService selectionService = (ISelectionService)HandlerUtil.getActiveWorkbenchWindow(event).
					getSelectionService();
			ISelection selection = selectionService.getSelection();
			TreeSelection treeSelection = (TreeSelection)selection;
			Object firstElement = treeSelection.getFirstElement();
			
			if(firstElement instanceof IResource) {
				(new OpenDirectoryVisualization()).openEditorWithFile((IResource) firstElement, event);
			} else {
				
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExecutionException("Error opening the SAMPLER Editor", e);
		}
	}

}
