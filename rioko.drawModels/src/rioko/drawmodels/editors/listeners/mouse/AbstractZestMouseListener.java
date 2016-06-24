package rioko.drawmodels.editors.listeners.mouse;

import org.eclipse.swt.events.MouseListener;

import rioko.drawmodels.editors.listeners.AbstractZestListener;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.zest.ExtendedGraphViewer;

public abstract class AbstractZestMouseListener extends AbstractZestListener implements MouseListener {

	public AbstractZestMouseListener() {
		super();
	}
	
	public AbstractZestMouseListener(ZestEditor controller, ExtendedGraphViewer viewer) {
		super(controller, viewer);
	}
	

	@Override
	protected void doAddListener() {
		this.getViewer().getControl().addMouseListener(this);
	}

}
