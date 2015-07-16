package rioko.graphabstraction.diagram.iterators;

import rioko.grapht.AdjacencyListGraph;
import rioko.grapht.Edge;
import rioko.searchers.breadth.UndirectedSearchIterator;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;

public class TreeUndirectedSearchIterator <V extends DiagramNode> extends UndirectedSearchIterator<V> {

	public TreeUndirectedSearchIterator(AdjacencyListGraph<V,? extends Edge<V>> graph, V root) {
		super(graph, root, false);
	}
	
	public TreeUndirectedSearchIterator(AdjacencyListGraph<V,? extends Edge<V>> graph, V root, boolean searchAll) {
		super(graph, root, searchAll);
	}

	@Override
	protected boolean isValidEdge(Edge<V> edge, V currentVertex) {
		return (!edge.getType().equals(typeOfConnection.REFERENCE));
	}
}
