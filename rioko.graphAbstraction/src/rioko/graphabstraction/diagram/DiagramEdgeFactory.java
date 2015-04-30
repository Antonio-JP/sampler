package rioko.graphabstraction.diagram;

import org.jgrapht.EdgeFactory;

import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;

class DiagramEdgeFactory<V extends DiagramNode> implements EdgeFactory<V, DiagramEdge<V>> {

	@Override
	public DiagramEdge<V> createEdge(V arg0, V arg1) {
		return new DiagramEdge<V>(arg0, arg1);
	}

}
