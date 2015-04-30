package rioko.graphabstraction.diagram.iterators;

import rioko.grapht.AbstractGraph;
import rioko.searchers.breadth.UndirectedSearchIterator;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;

public class TreeUndirectedSearchIterator <V extends DiagramNode,E extends DiagramEdge<V>> extends UndirectedSearchIterator<V,E> {

	public TreeUndirectedSearchIterator(AbstractGraph<V,E> graph, V root) {
		super(graph, root, false);
	}
	
	public TreeUndirectedSearchIterator(AbstractGraph<V,E> graph, V root, boolean searchAll) {
		super(graph, root, searchAll);
	}

	@Override
	protected boolean isValidEdge(E edge, V currentVertex) {
		return (!edge.getType().equals(typeOfConnection.REFERENCE));
	}
}
