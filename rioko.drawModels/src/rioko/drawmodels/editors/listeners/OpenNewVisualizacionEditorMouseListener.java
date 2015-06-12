package rioko.drawmodels.editors.listeners;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.iterators.TreeDirectedSearchIterator;
import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.drawmodels.diagram.XMIDiagram.XMIProxyDiagramNode;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.zest.ExtendedGraphViewer;

public class OpenNewVisualizacionEditorMouseListener implements MouseListener {

	private ZestEditor controler;
	private ExtendedGraphViewer viewer;
	
	public OpenNewVisualizacionEditorMouseListener(ZestEditor controler, ExtendedGraphViewer viewer)
	{
		this.controler = controler;
		this.viewer = viewer;
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent me) {
		if(me.button == 3) { //Doble click derecho
			for(IFigure figure : this.viewer.getFiguresAtPosition(me.x, me.y))	//Buscamos en las figuras clicadas
			{
				if(figure instanceof ModelNodeFigure)	//Si son un nodo
				{
					ModelNodeFigure mdFig = (ModelNodeFigure)figure;
					if(mdFig.getReferredNode() instanceof ComposeDiagramNode) {	//Y si son un nodo compuesto
						ComposeDiagramNode compose = (ComposeDiagramNode)mdFig.getReferredNode();
						
						this.controler.createNavigationEditor(compose);	//Abrimos la nueva ventana
						return;
					} else if(mdFig.getReferredNode() instanceof XMIProxyDiagramNode) {
						XMIProxyDiagramNode proxy = (XMIProxyDiagramNode)mdFig.getReferredNode();
						
						this.controler.createNavigationEditor(proxy);
					}
				}
			}
		}
		if(me.button == 2) { //Doble click central
			for(IFigure figure : this.viewer.getFiguresAtPosition(me.x, me.y))	//Buscamos en las figuras clicadas
			{
				if(figure instanceof ModelNodeFigure)	//Si son un nodo
				{
					ModelNodeFigure mdFig = (ModelNodeFigure)figure;
					DiagramGraph graph = this.controler.getModel().getModelDiagram();
					DiagramNode root = mdFig.getReferredNode().getRootNode();
					this.controler.createNavigationEditor(graph.getSubTree(new TreeDirectedSearchIterator<DiagramNode, DiagramEdge<DiagramNode>>(graph, root))); 
					
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

}
