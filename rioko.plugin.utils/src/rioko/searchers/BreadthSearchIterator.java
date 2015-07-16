package rioko.searchers;

import java.util.LinkedList;
import java.util.Queue;

import rioko.utilities.Pair;
import rioko.grapht.AdjacencyListGraph;
import rioko.grapht.Edge;
import rioko.grapht.Vertex;

/**
 * Breadth search algorithm used in graphs of the class {@link rioko.grapht.AdjacencyListGraph AbstractGraph}&#60;V,E&#62;.
 * 
 * @author Antonio
 *
 * @param <V> Class of the vertex of the graph.
 */
public abstract class BreadthSearchIterator<V extends Vertex> extends SearchIterator<V> {
	
	/**
	 * Graph this searcher will iterate.
	 */
	private AdjacencyListGraph<V, ? extends Edge<V>> toIterate = null;
	
	/**
	 * Queue used in the breadth algorithm
	 */
	private Queue<Pair<V,V>> queue;
	
	/**
	 * Parameter to search different connected components of the graph or not.
	 */
	private boolean searchAll = true;
	
	/**
	 * Parameter to know if we have finished the first connected component.
	 */
	private boolean isOtherComponent = false;
	
	//Builders
	public BreadthSearchIterator(AdjacencyListGraph<V, ? extends Edge<V>> graph, V root)
	{
		this.toIterate = graph;
		
		this.queue = new LinkedList<>();
		
		//We add the root node to the queue
		this.queue.add(new Pair<V,V>(root,null));
	}
	
	//Getters and Setters
	/**
	 * Setter method for parameter {@link rioko.searchers.BreadthSearchIterator#searchAll searchAll}
	 * 
	 * @param searchAll boolean which will be establish as value for the parameter.
	 */
	protected void setSearchAll(boolean searchAll) {
		this.searchAll = searchAll;
	}
	
	//Other methods
	/**
	 * Method to know if a vertex is valid for the breadth algorithm
	 * 
	 * @param vertex Vertex we want to check.
	 * 
	 * @return True if the vertex should be add to the queue when the algorithm reach it and false otherwise.
	 */
	protected boolean isValidVertex(V vertex) {
		return this.searchAll || (!this.isOtherComponent);
	}
	
	//Abstract methods
	/**
	 * Abstract method to know if an edge is valid to explore in the breadth algorithm.
	 * 
	 * @param edge Edge we want to check.
	 * @param currentVertex Vertex where the algorithm is exploring currently.
	 * 
	 * @return True if the edge should be explore in this step of the algorithm.
	 */
	protected abstract boolean isValidEdge(Edge<V> edge, V currentVertex);
	
	//Iterator methods
	@Override
	public boolean hasNext() {
		while((!this.queue.isEmpty()) &&
				((!this.isValidVertex(queue.peek().getFirst())) || 
				(this.parentOf.containsKey(queue.peek().getFirst())))) {
			this.queue.poll();
		}
		
		//We search throught all connected components of the graph
		if(queue.isEmpty()) {
			this.isOtherComponent = true;
			for(V node : this.toIterate.vertexSet()) {
				if(!parentOf.containsKey(node) && this.isValidVertex(node)) {
					queue.add(new Pair<V,V>(node,null));
					break;
				}
			}
		}
		
		return !queue.isEmpty();
	}

	@Override
	public V next() {
		//We get out the next item of the queue
		Pair<V,V> res;
	
		res = queue.poll();
		
		//We put the vertex and its parent in the HashMap
		this.parentOf.put(res.getFirst(), res.getLast());
		
		//We add the adyacent vertices to the queue
		for(Edge<V> edge : toIterate.edgesFrom(res.getFirst())) {
			if(this.isValidEdge(edge, res.getFirst())) {
				//We check in runtime that the casting is correct
				try {
					queue.add(new Pair<V,V>(edge.getTarget(), res.getFirst()));
				} catch (ClassCastException e) {
					e.printStackTrace();
				}
			}
		}
		//We add the adyacent vertices to the queue
		for(Edge<V> edge : toIterate.edgesTo(res.getFirst())) {
			if(this.isValidEdge(edge, res.getFirst())) {
				//We check in runtime that the casting is correct
				try {
					queue.add(new Pair<V,V>(edge.getSource(), res.getFirst()));
				} catch (ClassCastException e) {
					e.printStackTrace();
				}
			}
		}
		return res.getFirst();
	}

}
