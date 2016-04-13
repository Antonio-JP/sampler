package rioko.grapht;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import rioko.utilities.collections.ListSet;

/**
 * Interface that gives the methods a Graph should implement to be a real Graph. It has two 
 * parameters: the kind of vertices that contains, and the edges that join those vertices.
 * 
 * @author Antonio
 *
 * @param <V> Class of vertices this Graph will contain (extends {@link rioko.grapht.Vertex}.
 * @param <E> Class of edges this Graph will have (extends {@link rioko.grapht.Edge}&#60;V&#62;.
 */
public interface Graph<V extends Vertex,E extends Edge<V>> {
	//Graph management methods
	/**
	 * Method to add a new Vertex to the graph.
	 * 
	 * @param vertex The vertex we will add.
	 * 
	 * @return True if the vertex have been added and false if the vertex already was in the Graph.
	 */
	public boolean addVertex(V vertex);
	/**
	 * Method that add a new edge that join the parameter vertices. The two vertices should exist in 
	 * the Graph. If the edge already exists, this method do nothing.
	 * 
	 * @param source Vertex from the edge will go.
	 * @param target Vertex to the edge will go.
	 * 
	 * @return An edge in the graph that joins source to target or null if there is any error.
	 */
	public E addEdge(V source, V target);

	/**
	 * Method that remove a vertex from the graph. If the graph does not contain the vertex, this 
	 * method do nothing. This method remove all the edges of the graph that have as source or target
	 * the vertex.
	 *  
	 * @param vertex Vertex we want to remove.
	 * 
	 * @return True if the vertex was successfully removed and false if the graph does not contain the vertex.
	 */
	public boolean removeVertex(V vertex);
	/**
	 * Method that removes a collection of vertices. It calls the method {@link rioko.grapht.Graph#removeVertex(Vertex)}.
	 * 
	 * @param vertices Collection of vertices to remove.
	 * 
	 * @return True if the graph has changed and false otherwise.
	 */
	public default boolean removeAllVertices(Collection<? extends V> vertices) {
		boolean res = false;
		
		for(V vertex : vertices) {
			res |= this.removeVertex(vertex);
		}
		
		return res;
	}
	/**
	 * Method to remove an edge of the graph.
	 * 
	 * @param edge Edge we want to remove from the graph.
	 * 
	 * @return True if the edge was successfully removed and false if the edge was not contained 
	 * in the graph.
	 */
	public boolean removeEdge(E edge);
	/**
	 * Method to remove an edge given the source and the target vertices. It returns the edge removed,
	 * so returns null when the edge does not exist.
	 * 
	 * @param source Source vertex of the edge we want remove.
	 * @param target Target vertex of the edge we want remove.
	 * 
	 * @return The edge removed and null if it does not exist.
	 */
	public default E removeEdge(V source, V target) {
		if(!this.containsEdge(source, target)) {
			return null;
		}
		
		E edge = this.getEdge(source, target);
		this.removeEdge(edge);
		
		return edge;
	}
	/**
	 * Method that removes a collection of vertices. It calls the method {@link rioko.grapht.Graph#removeEdge(Edge)}.
	 * @param edges Collection of edges to remove.
	 * 
	 * @return True if the graph has changed and false otherwise.
	 */
	public default boolean removeAllEdges(Collection<? extends E> edges) {
		boolean res = false;
		
		for(E edge : edges) {
			res |= this.removeEdge(edge);
		}
		
		return res;
	}
	/**
	 * Method that remove the edges related to two vertices. As these graphs are not multi-graphs, this method
	 * will remove two, one or none edges.
	 * 
	 * @param ver1 One vertex.
	 * @param ver2 Another vertex.
	 * 
	 * @return The set of edges removed from the graph or an empty set if no edge has been removed.
	 */
	public default Set<E> removeAllEdges(V ver1, V ver2) {
		Set<E> edges = this.getAllEdges(ver1, ver2);
		
		this.removeAllEdges(edges);
		
		return edges;
	}
	

	/**
	 * Method to get the Vertex Factory to create new edges for this graph.
	 * 
	 * @return A factory of type {@link rioko.grapht.VertexFactory}&#60;V&#62; 
	 */
	public VertexFactory<V> getVertexFactory();
	/**
	 * Method to get the Edge Factory to create new edges for this graph.
	 * 
	 * @return A factory of type {@link rioko.grapht.EdgeFactory}&#60;V,E&#62; 
	 */
	public EdgeFactory<V,E> getEdgeFactory();
	
	//Graph content methods
	/**
	 * Method that returns the set of Vertex contained in this graph.
	 * 
	 * @return Set of the vertex.
	 */
	public Set<V> vertexSet();
	/**
	 * Method that returns the set of Edges contained in this graph.
	 * 
	 * @return Set of the vertex.
	 */
	public Set<E> edgeSet();

	/**
	 * Getter method for an edge of the graph joining two vertices of the graph.
	 * 
	 * @param source Source vertex of the edge returned.
	 * @param target Target vertex of the edge returned.
	 * 
	 * @return The edge joining the two parameters. It returns null if any vertex
	 * are not included in the graph or if the edge does not exist.
	 */
	public default E getEdge(V source, V target) {
		if(!this.vertexSet().contains(source) || !this.vertexSet().contains(target)) {
			return null;
		}
		
		for(E edge : this.edgesFrom(source)) {
			if(edge.getTarget().equals(target)) {
				return edge;
			}
		}
		
		return null;
	}
	/**
	 * Method to get the source vertex of an edge of the graph.
	 * 
	 * @param edge Edge where we want to look.
	 * 
	 * @return The source vertex of the parameter. Null if the edge is not included 
	 * in the graph.
	 */
	public default V getEdgeSource(E edge) {
		if(!this.containsEdge(edge)) {
			return null;
		}
		
		return edge.getSource();
	}
	/**
	 * Method to get the target vertex of an edge of the graph.
	 * 
	 * @param edge Edge where we want to look.
	 * 
	 * @return The target vertex of the parameter. Null if the edge is not included 
	 * in the graph.
	 */
	public default V getEdgeTarget(E edge) {
		if(!this.containsEdge(edge)) {
			return null;
		}
		
		return edge.getTarget();
	}
	/**
	 * Method that gets the weight of a particular edge of the graph.
	 * 
	 * @param edge Edge we want to check.
	 * 
	 * @return Double value with the weight.
	 */
	public default double getEdgeWeight(E edge) {
		return 1.0;
	}

	/**
	 * Method to check if a vertex is contained in the graph,
	 * 
	 * @param vertex Vertex to check.
	 * 
	 * @return True if the vertex is in the graph and false otherwise.
	 */
	public boolean containsVertex(V vertex);
	/**
	 * Method to check if an edge is contained in the graph,
	 * 
	 * @param edge Edge to check.
	 * 
	 * @return True if the edge is in the graph and false otherwise.
	 */
	public default boolean containsEdge(E edge) {
		return this.containsEdge(edge.getSource(), edge.getTarget());
	}
	/**
	 * Method to check if an edge  with a source and target vertices is contained in the graph,
	 * 
	 * @param source Source vertex of the edge searched.
	 * @param target Target vertex of the edge searched.
	 * 
	 * @return True if the edge (source,target) is in the graph and false otherwise.
	 */
	public default boolean containsEdge(V source, V target) {
		return this.getEdge(source, target) != null;
	}

	/**
	 * Method that return a set of edge where everyone has a certain source vertex.
	 * 
	 * @param vertex Vertex we search as source.
	 * 
	 * @return Set of edges where for all edge in it, "edge.getSource().equals(vertex)" is true.
	 */
	public default Set<E> edgesFrom(V vertex) {
		Set<E> res = new ListSet<E>();
		
		for(E edge : this.edgeSet()) {
			if(edge.getSource().equals(vertex)) {
				res.add(edge);
			}
		}
		
		return res;
	}
	/**
	 * Method that return a set of edge where everyone has a certain target vertex.
	 * 
	 * @param vertex Vertex we search as target.
	 * 
	 * @return Set of edges where for all edge in it, "edge.getTarget().equals(vertex)" is true.
	 */
	public default Set<E> edgesTo(V vertex) {
		Set<E> res = new ListSet<E>();
		
		for(E edge : this.edgeSet()) {
			if(edge.getTarget().equals(vertex)) {
				res.add(edge);
			}
		}
		
		return res;
	}
	/**
	 * Method to get a set with all the edges that has as source or target a certain vertex.
	 * 
	 * @param vertex Vertex we are searching
	 * 
	 * @return Set of edges where, for all edge in it:
	 * 	[edge.getTarget().equals(vertex) || edge.getDource().equals(vertex)] == true 
	 */
	public default Set<E> edgesOf(V vertex) {
			Set<E> set = this.edgesFrom(vertex);
			set.addAll(this.edgesTo(vertex));
			
			return set; 
	}
	/**
	 * Method to get all the edges related with two vertices at the same time. This means that this method
	 * returns a set of edge contain in the set:
	 * 	{(ver1,ver2), (ver2,ver1)}
	 * 
	 * @param ver1 A vertex to search.
	 * @param ver2 Another vertex to search.
	 * 
	 * @return Set of edges with zero, one or two elements.
	 */
	public default Set<E> getAllEdges(V ver1, V ver2) {
		Set<E> edges = this.edgesOf(ver1);
		Set<E> res = new ListSet<E>();
		
		for(E edge : edges) {
			if(this.getEdgeSource(edge).equals(ver2) || this.getEdgeTarget(edge).equals(ver2)) {
				res.add(edge);
			}
		}
		
		return res;
	}
	
	/**
	 * Method to get all the vertices we can reach from a certain vertex.
	 * 
	 * @param vertex Vertex from we want to find another vertices.
	 * 
	 * @return A set of vertices that, for all vertex V in it:
	 * 	this.containsEdge(vertex, V) == true
	 */
	public default Set<V> vertexFrom(V vertex)  {
		Set<V> set = new HashSet<V>();
		
		for(E edge : this.edgesFrom(vertex)) {
			set.add(edge.getTarget());
		}
		
		return set;
	}	
	/**
	 * Method to get all the vertices from we can reach a certain vertex.
	 * 
	 * @param vertex Vertex we want to find from another vertices.
	 * 
	 * @return A set of vertices that, for all vertex V in it:
	 * 	this.containsEdge(V, vertex) == true
	 */
	public default Set<V> vertexTo(V vertex) {
		Set<V> set = new HashSet<V>();
		
		for(E edge : this.edgesTo(vertex)) {
			set.add(edge.getSource());
		}
		
		return set;
	}
	
	//Other methods
	/**
	 * Method to get the Vertex class of the Graph. It must be the parameter V of the Graph class.
	 * 
	 * @return Class of the Vertices
	 */
	public Class<? extends V> getVertexClass();
	
	/**
	 * Method to get the Edge class of the Graph. It must be the parameter E of the Graph class.
	 * 
	 * @return Class of the Edges
	 */
	public Class<? extends E> getEdgeClass();
}
