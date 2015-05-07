package rioko.searchers.breadth;

import rioko.grapht.AbstractGraph;
import rioko.grapht.Vertex;
import rioko.grapht.VisibleEdge;
import rioko.searchers.BreadthSearchIterator;

public class UndirectedSearchIterator <V extends Vertex,E extends VisibleEdge<V>> extends BreadthSearchIterator<V,E> {

	public UndirectedSearchIterator(AbstractGraph<V,E> graph, V root) {
		super(graph, root);
	}
	
	public UndirectedSearchIterator(AbstractGraph<V,E> graph, V root, boolean searchAll) {
		super(graph, root);
		
		this.setSearchAll(searchAll);
	}
	
	@Override
	protected boolean isValidEdge(E edge, V currentVertex) {
		return true;
	}
}
