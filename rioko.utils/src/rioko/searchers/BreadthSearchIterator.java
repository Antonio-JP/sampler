package rioko.searchers;

import java.util.LinkedList;
import java.util.Queue;

import rioko.utilities.Pair;
import rioko.grapht.AbstractGraph;
import rioko.grapht.Vertex;
import rioko.grapht.VisibleEdge;

public abstract class BreadthSearchIterator<V extends Vertex,E extends VisibleEdge<V>> extends SearchIterator<V> {
	
	private AbstractGraph<V, E> toIterate = null;
	
	private Queue<Pair<V,V>> queue;
	
	private boolean searchAll = true;
	
	private boolean isOtherComponent = false;
	
	//Builders
	public BreadthSearchIterator(AbstractGraph<V, E> graph, V root)
	{
		this.toIterate = graph;
		
		this.queue = new LinkedList<>();
		
		//We add the root node to the queue
		this.queue.add(new Pair<V,V>(root,null));
	}
	
	//Getters and Setters
	protected void setSearchAll(boolean searchAll) {
		this.searchAll = searchAll;
	}
	
	//Other methods
	protected boolean isValidVertex(V vertex) {
		return this.searchAll || (!this.isOtherComponent);
	}
	
	//Abstract methods
	protected abstract boolean isValidEdge(E edge, V currentVertex);
	
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
		for(E edge : toIterate.edgesFrom(res.getFirst())) {
			if(this.isValidEdge(edge, res.getFirst())) {
				//We check in runtime that the casting is correct
				try {
					queue.add(new Pair<V,V>((V)edge.getTarget(), res.getFirst()));
				} catch (ClassCastException e) {
					e.printStackTrace();
				}
			}
		}
		//We add the adyacent vertices to the queue
		for(E edge : toIterate.edgesTo(res.getFirst())) {
			if(this.isValidEdge(edge, res.getFirst())) {
				//We check in runtime that the casting is correct
				try {
					queue.add(new Pair<V,V>((V)edge.getSource(), res.getFirst()));
				} catch (ClassCastException e) {
					e.printStackTrace();
				}
			}
		}
		return res.getFirst();
	}

}
