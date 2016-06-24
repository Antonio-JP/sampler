package rioko.drawmodels.editors.listeners.mouse;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.events.MouseEvent;
import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.jface.ZestEditorStructuredSelection;
import rioko.zest.ExtendedGraphViewer;

public class ZestClickSelectionMouseListener extends AbstractZestMouseListener {

	//Builders
	public ZestClickSelectionMouseListener() {
		super();
	}
	
	public ZestClickSelectionMouseListener(ZestEditor controller, ExtendedGraphViewer viewer) {
		super(controller,viewer);
	}
	
	//Methods
	@Override
	public void mouseDoubleClick(MouseEvent me) {
		//Nothing happens
	}

	@Override
	public void mouseDown(MouseEvent me) {
		if(me.button == 1) {
			//Get the selection (if happens)
			IFigure[] figures = this.getViewer().getFiguresAtPosition(me.x, me.y);
			
			for(int i = 0; i < figures.length; i++) {
				if(figures[i] instanceof ModelNodeFigure) {
					this.getController().setSelection(new ZestEditorStructuredSelection((ModelNodeFigure)figures[i], this.getController()));
					break;
				}
			}
		}
	}

	@Override
	public void mouseUp(MouseEvent me) {
		//Nothing happens
	}

	@Override
	protected void doAddListener() {
		this.getViewer().getControl().addMouseListener(this);
	}

}
