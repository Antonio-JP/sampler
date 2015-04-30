package rioko.drawmodels.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public abstract class AbstractGenericHandler extends AbstractHandler {

	protected IWorkbenchPage getEditorPage() throws Exception
	{
		IWorkbench wb = PlatformUI.getWorkbench();
		if(wb == null) {
			throw new Exception("Rioko ERROR - Handlers: No Workbench found!");
		}

		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();	
		if(win == null) {
			throw new Exception("Rioko ERROR - Handlers: No Window found!");
		}
		
		IWorkbenchPage page = win.getActivePage();
		if(page == null) {
			throw new Exception("Rioko ERROR - Handlers: No Page Found!");
		}
		
		return page;
	}
	
	protected IFile getFile(String path) throws Exception
	{
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFile file = root.getFile(new Path(path));
		
		if(!file.exists()) {
			throw new Exception("Rioko ERROR - Handlers: File does not exists!");
		}

		return file;
	}

}
