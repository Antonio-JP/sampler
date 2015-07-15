package rioko.gmldrawer.gmlDiagram.factories;

import org.jgrapht.EdgeFactory;

import rioko.gmldrawer.gmlDiagram.GMLDiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;

public class GMLDiagramEdgeFactory implements EdgeFactory<DiagramNode, GMLDiagramEdge> {

	@Override
	public GMLDiagramEdge createEdge(DiagramNode arg0, DiagramNode arg1) {
		return new GMLDiagramEdge(arg0, arg1);
	}

}
