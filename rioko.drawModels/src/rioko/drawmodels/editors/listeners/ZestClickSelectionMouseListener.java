package rioko.drawmodels.editors.listeners;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.jface.ZestEditorStructuredSelection;
import rioko.zest.ExtendedGraphViewer;

public class ZestClickSelectionMouseListener implements MouseListener {

	private ZestEditor controler;
	private ExtendedGraphViewer viewer;
	
	public ZestClickSelectionMouseListener(ZestEditor controler, ExtendedGraphViewer viewer)
	{
		this.controler = controler;
		this.viewer = viewer;
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent me) {
		//Nothing happens
	}

	@Override
	public void mouseDown(MouseEvent me) {
		if(me.button == 1) {
			//Get the selection (if happens)
			IFigure[] figures = this.viewer.getFiguresAtPosition(me.x, me.y);
			
			for(int i = 0; i < figures.length; i++) {
				if(figures[i] instanceof ModelNodeFigure) {
					this.controler.setSelection(new ZestEditorStructuredSelection((ModelNodeFigure)figures[i], this.controler));
					break;
				}
			}
		}
	}

	@Override
	public void mouseUp(MouseEvent me) {
		//Nothing happens
	}

}
