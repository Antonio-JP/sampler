package rioko.drawmodels.editors.listeners.mouse;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.events.MouseEvent;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.ProxyDiagramNode;
import rioko.graphabstraction.diagram.iterators.TreeDirectedSearchIterator;
import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.zest.ExtendedGraphViewer;

public class OpenNewVisualizacionEditorMouseListener extends AbstractZestMouseListener {
	
	//Builders
	public OpenNewVisualizacionEditorMouseListener() {
		super();
	}

	public OpenNewVisualizacionEditorMouseListener(ZestEditor controller, ExtendedGraphViewer viewer) {
		super(controller, viewer);
	}
	
	//Methods
	@Override
	public void mouseDoubleClick(MouseEvent me) {
		if(me.button == 3) { //Doble click derecho
			for(IFigure figure : this.getViewer().getFiguresAtPosition(me.x, me.y))	//Buscamos en las figuras clicadas
			{
				if(figure instanceof ModelNodeFigure)	//Si son un nodo
				{
					ModelNodeFigure mdFig = (ModelNodeFigure)figure;
					if(mdFig.getReferredNode() instanceof ComposeDiagramNode) {	//Y si son un nodo compuesto
						ComposeDiagramNode compose = (ComposeDiagramNode)mdFig.getReferredNode();
						
						this.getController().createNavigationEditor(compose);	//Abrimos la nueva ventana
						return;
					} else if(mdFig.getReferredNode() instanceof ProxyDiagramNode<?>) {
						ProxyDiagramNode<?> proxy = (ProxyDiagramNode<?>)mdFig.getReferredNode();
						
						this.getController().createNavigationEditor(proxy);
					}
				}
			}
		}
		if(me.button == 2) { //Doble click central
			for(IFigure figure : this.getViewer().getFiguresAtPosition(me.x, me.y))	//Buscamos en las figuras clicadas
			{
				if(figure instanceof ModelNodeFigure)	//Si son un nodo
				{
					ModelNodeFigure mdFig = (ModelNodeFigure)figure;
					DiagramGraph graph = this.getController().getModel().getModelDiagram();
					DiagramNode root = mdFig.getReferredNode().getRootNode();
					this.getController().createNavigationEditor(graph.getSubTree(new TreeDirectedSearchIterator<DiagramNode>(graph, root))); 
					
					break;
				}
			}
		}
	}

	@Override
	public void mouseDown(MouseEvent me) {
		//Nothing happens
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
