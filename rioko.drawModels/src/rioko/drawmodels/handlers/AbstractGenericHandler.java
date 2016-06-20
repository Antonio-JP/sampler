package rioko.drawmodels.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class AbstractGenericHandler extends AbstractHandler {
	
	private IWorkbench wb = null;
	private IWorkbenchWindow win = null;
	private IWorkbenchPage page = null;
	
	protected void setContext(ExecutionEvent ee) throws Exception {
		this.win = HandlerUtil.getActiveWorkbenchWindow(ee);
		this.page = this.getEditorPage();
	}

	protected IWorkbench getWorkbench() throws Exception
	{
		if(this.wb == null) {
			wb = PlatformUI.getWorkbench();
			if(wb == null) {
				throw new Exception("Rioko ERROR - Handlers: No Workbench found!");
			}
		}
		
		return wb;
	}
	
	protected IWorkbenchWindow getWindow() throws Exception
	{
		if(this.win == null) {
			IWorkbench wb = this.getWorkbench();
	
			win = wb.getActiveWorkbenchWindow();	
			if(win == null) {
				throw new Exception("Rioko ERROR - Handlers: No Window found!");
			}
		}
		
		return win;
	}
	
	protected IWorkbenchPage getEditorPage() throws Exception
	{
		if(this.page == null) {
			IWorkbenchWindow win = this.getWindow();
			
			page = win.getActivePage();
			if(page == null) {
				throw new Exception("Rioko ERROR - Handlers: No Page Found!");
			}
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
