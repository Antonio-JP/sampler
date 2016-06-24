package rioko.drawmodels.editors.listeners.mouse;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.events.MouseEvent;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.ProxyDiagramNode;
import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.zest.ExtendedGraphViewer;
import rioko.drawmodels.editors.zesteditor.ZestEditor;

/**
 * MouseListener used by the ZestEditor to change the root for aggregation algorithm.
 * 
 * @author Antonio
 */
public class ExpandNodesMouseListener extends AbstractZestMouseListener {

	//Builders
	public ExpandNodesMouseListener() {
		super();
	}
	
	public ExpandNodesMouseListener(ZestEditor controller) {
		super(controller, null);
	}
	
	public ExpandNodesMouseListener(ZestEditor controller, ExtendedGraphViewer viewer)
	{
		super(controller, viewer);
	}

	//MouseListener Interface methods
	@Override
	public void mouseDoubleClick(MouseEvent me) {	
		if(me.button == 1) {
			//Encontramos el nodo adecuado
			IFigure[] figures = this.getController().getViewer().getFiguresAtPosition(me.x, me.y);
			
			if(figures.length > 0)
			{
				for(IFigure fig : figures)
				{
					if(fig instanceof ModelNodeFigure)
					{
						ModelNodeFigure ndFig = (ModelNodeFigure)fig;
						
						if(ndFig.getReferredNode() instanceof ComposeDiagramNode) {
							this.getController().extendComposeNode((ComposeDiagramNode)ndFig.getReferredNode());
						} else if(ndFig.getReferredNode() instanceof ProxyDiagramNode){
							this.getController().extendProxyNode((ProxyDiagramNode<?>)ndFig.getReferredNode().getRootNode());
						} //else {
							//this.getController().changeRoot(ndFig.getReferredNode().getRootNode());
						//}
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
