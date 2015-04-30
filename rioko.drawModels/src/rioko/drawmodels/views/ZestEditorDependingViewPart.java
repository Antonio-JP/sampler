package rioko.drawmodels.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import rioko.drawmodels.editors.zesteditor.ZestEditor;

public abstract class ZestEditorDependingViewPart extends ViewPart implements IPartListener{

	private ZestEditor currentEditor = null;
	
	protected ZestEditor getCurrentEditor()
	{
		return this.currentEditor;
	}
	
	//ViewPart abstract methods
	@Override
	public void createPartControl(Composite arg0) {
		//Añadimos a la lista de wizardListener de la ventana
		((IPartService)this.getSite().getService(IPartService.class)).addPartListener(this);
	}

	@Override
	public void setFocus() { }
	
	//IPartListener methods
	@Override
	public void partActivated(IWorkbenchPart part) {
		if(part != null) {
			if(this.currentEditor != part) {
				if(part instanceof ZestEditor) {
					this.doBeforeChange(part);
					
					this.currentEditor = (ZestEditor)part;
					
					this.doWhenActivate(part);
				}
			}
			
			this.updateView();
		}
	}
	
	protected abstract void doBeforeChange(IWorkbenchPart part);

	protected abstract void doWhenActivate(IWorkbenchPart part);
	
	public abstract void updateView();

	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) { }

	@Override
	public void partClosed(IWorkbenchPart arg0) { }

	@Override
	public void partDeactivated(IWorkbenchPart arg0) { }

	@Override
	public void partOpened(IWorkbenchPart arg0) { }
	
	//Private methods
	protected boolean isActiveZest() {
		return this.getSite().getPage().getActiveEditor() instanceof ZestEditor;
	}
		
	protected ZestEditor getZestEditor() {
		return (ZestEditor)this.getSite().getPage().getActiveEditor();
	}

}
