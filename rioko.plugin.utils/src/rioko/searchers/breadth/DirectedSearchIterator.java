package rioko.searchers.breadth;

import rioko.grapht.AdjacencyListGraph;
import rioko.grapht.Edge;
import rioko.grapht.Vertex;
import rioko.searchers.BreadthSearchIterator;

/**
 * Breadth Searcher for graphs where an edge is valid if and only if the current vertex is the source of the edge.
 * 
 * @author Antonio
 *
 * @param <V> Class of the vertex of the graph.
 */
public class DirectedSearchIterator<V extends Vertex> extends BreadthSearchIterator<V> {

	public DirectedSearchIterator(AdjacencyListGraph<V,? extends Edge<V>> graph, V root) {
		super(graph, root);
		
		this.setSearchAll(false);
	}
	
	public DirectedSearchIterator(AdjacencyListGraph<V,? extends Edge<V>> graph, V root, boolean searchAll) {
		super(graph, root);
		
		this.setSearchAll(searchAll);
	}

	@Override
	protected boolean isValidEdge(Edge<V> edge, V currentVertex) {
		return edge.getSource().equals(currentVertex);
	}
}
