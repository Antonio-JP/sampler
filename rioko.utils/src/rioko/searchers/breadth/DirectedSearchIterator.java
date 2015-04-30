package rioko.searchers.breadth;

import rioko.grapht.AbstractGraph;
import rioko.grapht.Vertex;
import rioko.grapht.VisibleEdge;
import rioko.searchers.BreadthSearchIterator;

public class DirectedSearchIterator<V extends Vertex,E extends VisibleEdge<V>> extends BreadthSearchIterator<V,E> {

	public DirectedSearchIterator(AbstractGraph<V,E> graph, V root) {
		super(graph, root);
		
		this.setSearchAll(false);
	}
	
	public DirectedSearchIterator(AbstractGraph<V,E> graph, V root, boolean searchAll) {
		super(graph, root);
		
		this.setSearchAll(searchAll);
	}

	@Override
	protected boolean isValidEdge(E edge, V currentVertex) {
		return edge.getSource().equals(currentVertex);
	}
}
