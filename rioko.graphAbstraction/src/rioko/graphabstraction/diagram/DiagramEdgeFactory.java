package rioko.graphabstraction.diagram;

import rioko.grapht.EdgeFactory;

import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;

class DiagramEdgeFactory<V extends DiagramNode> implements EdgeFactory<V, DiagramEdge<V>> {

	@Override
	public DiagramEdge<V> createEdge(V source, V target) {
		return new DiagramEdge<V>(source, target);
	}

}
