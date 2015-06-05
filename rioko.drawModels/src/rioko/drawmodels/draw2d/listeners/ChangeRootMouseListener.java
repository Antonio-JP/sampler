package rioko.drawmodels.draw2d.listeners;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.drawmodels.diagram.XMIDiagram.XMIProxyDiagramNode;
import rioko.drawmodels.editors.zesteditor.ZestEditor;

/**
 * MouseListener used by the ZestEditor to change the root for aggregation algorithm.
 * 
 * @author Antonio
 */
public class ChangeRootMouseListener implements MouseListener {

	//Private attributes
	/**
	 * ZestEditor related with this wizardListener
	 */
	private ZestEditor controler;
	
	//Builders
	/**
	 * Public builder indicating the controler related with this wizardListener
	 * 
	 * @param controler ZestEditor to change
	 */
	public ChangeRootMouseListener(ZestEditor controler)
	{
		this.controler = controler;
	}

	//MouseListener Interface methods
	@Override
	public void mouseDoubleClick(MouseEvent me) {	
		if(me.button == 1) {
			//Encontramos el nodo adecuado
			IFigure[] figures = this.controler.getViewer().getFiguresAtPosition(me.x, me.y);
			
			if(figures.length > 0)
			{
				for(IFigure fig : figures)
				{
					if(fig instanceof ModelNodeFigure)
					{
						ModelNodeFigure ndFig = (ModelNodeFigure)fig;
						
						if(ndFig.getReferredNode() instanceof ComposeDiagramNode) {
							this.controler.extendComposeNode((ComposeDiagramNode)ndFig.getReferredNode());
						} else if(ndFig.getReferredNode() instanceof XMIProxyDiagramNode){
							this.controler.extendProxyNode((XMIProxyDiagramNode)ndFig.getReferredNode().getRootNode());
						} else {
							this.controler.changeRoot(ndFig.getReferredNode().getRootNode());
						}
						break;
					}
				}
			}
		}
	}

	@Override
	public void mouseDown(MouseEvent me) {
		// Do nothing		
	}

	@Override
	public void mouseUp(MouseEvent me) {
		// Do nothing		
	}

}
