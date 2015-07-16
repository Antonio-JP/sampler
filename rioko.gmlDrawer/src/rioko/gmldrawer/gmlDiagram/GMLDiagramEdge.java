package rioko.gmldrawer.gmlDiagram;

import rioko.gmldrawer.gmlDiagram.factories.GMLDiagramEdgeFactory;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;

public class GMLDiagramEdge extends DiagramEdge<DiagramNode> {
	
	public GMLDiagramEdge() { }
	
	public GMLDiagramEdge(DiagramNode source, DiagramNode target) {
		super(source, target);
	}
	
	@Override
	public GMLDiagramEdgeFactory getEdgeFactory() {
		return new GMLDiagramEdgeFactory();
	}
}
