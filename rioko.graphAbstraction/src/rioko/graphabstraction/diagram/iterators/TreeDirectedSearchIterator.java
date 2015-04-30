package rioko.graphabstraction.diagram.iterators;

import rioko.grapht.AbstractGraph;
import rioko.searchers.breadth.DirectedSearchIterator;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;

public class TreeDirectedSearchIterator<V extends DiagramNode,E extends DiagramEdge<V>> extends DirectedSearchIterator<V,E> {

	public TreeDirectedSearchIterator(AbstractGraph<V,E> graph, V root) {
		super(graph, root);
	}
	
	public TreeDirectedSearchIterator(AbstractGraph<V,E> graph, V root, boolean searchAll) {
		super(graph, root, searchAll);
	}

	@Override
	protected boolean isValidEdge(E edge, V currentVertex) {
		return (!edge.getType().equals(typeOfConnection.REFERENCE)) && edge.getSource().equals(currentVertex);
	}
}
