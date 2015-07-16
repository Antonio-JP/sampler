package rioko.searchers.breadth;

import rioko.grapht.AdjacencyListGraph;
import rioko.grapht.Edge;
import rioko.grapht.Vertex;
import rioko.searchers.BreadthSearchIterator;

/**
 * Breadth Searcher for graphs where every edge is valid to explore. This means that the search 
 * ignores the directions of the edges.
 * 
 * @author Antonio
 *
 * @param <V> Class of the vertex of the graph.
 */
public class UndirectedSearchIterator <V extends Vertex> extends BreadthSearchIterator<V> {

	public UndirectedSearchIterator(AdjacencyListGraph<V,? extends Edge<V>> graph, V root) {
		super(graph, root);
	}
	
	public UndirectedSearchIterator(AdjacencyListGraph<V,? extends Edge<V>> graph, V root, boolean searchAll) {
		super(graph, root);
		
		this.setSearchAll(searchAll);
	}
	
	@Override
	protected boolean isValidEdge(Edge<V> edge, V currentVertex) {
		return true;
	}
}
