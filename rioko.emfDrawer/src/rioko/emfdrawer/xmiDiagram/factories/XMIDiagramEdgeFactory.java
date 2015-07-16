package rioko.emfdrawer.xmiDiagram.factories;

import rioko.grapht.EdgeFactory;

import rioko.emfdrawer.xmiDiagram.XMIDiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;

public class XMIDiagramEdgeFactory implements EdgeFactory<DiagramNode, XMIDiagramEdge> {

	@Override
	public XMIDiagramEdge createEdge(DiagramNode arg0, DiagramNode arg1) {
		return new XMIDiagramEdge(arg0, arg1);
	}

}
