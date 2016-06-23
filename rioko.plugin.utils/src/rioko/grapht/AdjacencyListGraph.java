package rioko.grapht;

import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.ArrayList;

import rioko.grapht.EdgeFactory;
import rioko.grapht.Graph;

import rioko.searchers.breadth.DirectedSearchIterator;
import rioko.utilities.Log;
import rioko.utilities.collections.ArrayMap;
import rioko.utilities.collections.ListSet;

/**
 * Implementation of {@link rioko.grapht.Graph Graph} structure using adjacency lists to store the 
 * edges of the graph.
 * 
 * @author Antonio
 *
 * @param <V> Class of the Vertex of the Graph.
 * @param <E> Class of the Edges of the Graph.
 */
public class AdjacencyListGraph<V extends Vertex,E extends Edge<V>> implements Graph<V,E> {
	
	/**
	 * Map to the adjacency lists for each vertex of the graph.
	 */
	private Map<V,ListSet<E>> adyacencyList = new ArrayMap<>();
	
	/**
	 * Parameter to know if loops (edges with the same source and target) are allowed.
	 */
	private boolean loopsAvaible = false;
	
	/**
	 * Class of the edges of the graph
	 */
	protected Class<?> edgeClass = null;
	/**
	 * Class of the vertices of the graph
	 */
	private Class<?> vertexClass = null;
	
	/**
	 * Edge factory to create new edges for this graph
	 */
	private EdgeFactory<V,E> edgeFactory = null;
	/**
	 * Vertex factory to create new vertices for this graph
	 */
	private VertexFactory<V> vertexFactory = null;
	
	//Builders
	public AdjacencyListGraph(Class<?> edgeClass, Class<?> vertexClass) {
		this.edgeClass = edgeClass;
		this.vertexClass = vertexClass;
	}
	
	//Getters & Setters
	/**
	 * Setter method to the {@link rioko.grapht.AdjacencyListGraph#loopsAvaible loopsAvaible} parameter.
	 * 
	 * @param loopsAvaible Boolean value which will be set as 
	 * {@link rioko.grapht.AdjacencyListGraph#loopsAvaible loopsAvaible} parameter.
	 */
	protected void setLoopsAvaible(boolean loopsAvaible) {
		this.loopsAvaible = loopsAvaible;
	}
		
	//Graph Interface methods
	@Override
	public E addEdge(V arg0, V arg1) {
		//It is neccessary that the vertices are included in the graph
		if(!this.containsVertex(arg0) || ! this.containsVertex(arg1)) {
			return null;
		}
		
		//We check if it is a loop edge and if they are allowed
		if(!this.loopsAvaible && arg0.equals(arg1)) {
			return null;
		}
		
		//We check if the edge is already included in the graph
		E edge = this.getEdge(arg0, arg1);
		
		if(edge == null) {
			//If not, we create the new edge
			edge = this.getEdgeFactory().createEdge(arg0, arg1);
			this.adyacencyList.get(arg0).add(edge);
		}
		
		//Return the edge created
		return edge;
	}

	@Override
	public boolean addVertex(V arg0) {
		if(this.containsVertex(arg0)) {
			return false;
		}
		
		this.adyacencyList.put(arg0, new ListSet<E>());
		return true;
	}

	@Override
	public boolean containsVertex(V arg0) {
		return this.adyacencyList.keySet().contains(arg0);
	}

	@Override
	public Set<E> edgeSet() {
		Set<E> edges = new HashSet<E>();

		for(V vertex : this.adyacencyList.keySet()) {
			edges.addAll(this.adyacencyList.get(vertex));
		}
		
		return edges;
	}

	@SuppressWarnings("unchecked")
	@Override
	public EdgeFactory<V, E> getEdgeFactory() {
		if(this.edgeFactory == null) {
			try {
				this.edgeFactory = (EdgeFactory<V, E>) ((E) this.edgeClass.newInstance()).getEdgeFactory();
			} catch (InstantiationException | IllegalAccessException e) {
				Log.exception(e);
			}
		}
		return this.edgeFactory;
	}
	
	@SuppressWarnings("unchecked")
	public VertexFactory<V> getVertexFactory() {
		if(this.vertexFactory == null) {
			try {
				this.vertexFactory = (VertexFactory<V>) ((V)this.vertexClass.newInstance()).getVertexFactory();
			} catch (InstantiationException | IllegalAccessException e) {
				Log.exception(e);
			}
		}		
		return this.vertexFactory;
	}

	@Override
	public boolean removeEdge(E arg0) {
		if(this.containsEdge(arg0)) {
			this.adyacencyList.get(this.getEdgeSource(arg0)).remove(arg0);
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean removeVertex(V arg0) {
		if(!this.containsVertex(arg0))
		{
			return false;
		}
		
		for(V from : this.vertexTo(arg0)) {
			this.removeAllEdges(from, arg0);
		}
		
		this.adyacencyList.remove(arg0);
		return true;
	}

	@Override
	public Set<V> vertexSet() {
		return this.adyacencyList.keySet();
	}
	
	public Set<E> edgesFrom(V vertex) {
		return this.adyacencyList.get(vertex);
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends E> getEdgeClass()
	{
		return (Class<? extends E>)this.edgeClass;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends V> getVertexClass()
	{
		return (Class<? extends V>)this.vertexClass;
	}
	
	//Graph Operations
	/**
	 * Method to get the induced sub-graph given a collection of vertices. It requires 
	 * that the whole collection of vertices are included in the graph.
	 * 
	 *  The induced sub-graph has the vertices given as parameter and all the edges whose 
	 *  source and target are elements of the collection of vertices.
	 *  
	 * @param nodes Collection of vertices which will be in the induced sub-graph
	 * 
	 * @return A new Graph of the same kind of this with the induced sub-graph.
	 */
	@SuppressWarnings("unchecked")
	public AdjacencyListGraph<V,E> inducedSubgraph(Collection<V> nodes)
	{
		AdjacencyListGraph<V, E> result = null;

		try {
			//Comprobamos primero que todos los vértices que queremos están dentro del grafo
			for(V node : nodes) {
				if(!this.containsVertex(node)) {	//Error: no podemos inducir un subgrafo
					throw new Exception("Rioko ERROR: algún nodo no está contenido en el grafo luego no hay subgrafo.");
				}
			}
			
			result = this.getSimilarType();
		
			for(V node : nodes) {
				result.addVertex((V)node.copy());
			}
			
			for(V src : nodes) {
				for(V trg : this.vertexFrom(src)) {
					if(nodes.contains(trg)) {
						E edge = result.addEdge(src, trg);
						edge.setType(this.getEdge(src, trg).getType());
					}
				}
			}
		} catch (Exception e) {
			Log.exception(e);
		}
	
		
		return result;
	}
	
	/**
	 * Method that gives the vertices of the graph that, given an Interator over the vertices, are found
	 * iterating it.
	 * 
	 * @param iterator Iterator that will be iterated until it give no more vertices.
	 * 
	 * @return Collection with the vertices found.
	 */
	public Collection<V> getSubTree(DirectedSearchIterator<V> iterator) {
		//Haremos una búesqueda en anchura usando como criterio que las aristas sean de contención
		ArrayList<V> subTreeNodes = new ArrayList<>();
		
		while(iterator.hasNext()) {
			subTreeNodes.add(iterator.next());
		}
		
		return subTreeNodes;
	}
	
	/**
	 * Method that gets the connected components of the graph. It is possible 
	 * get Strongly Connected Components or just the Connected Components. 
	 * 
	 * It uses the Tarjan algorithm.
	 * 
	 * @param strongConnected Parameter indicating if get Strongly or Normal Connected components
	 * 
	 * @return A set with the Connected Components (as Sets of vertices).
	 */
	public Set<Set<V>> getConnectedComponents(Boolean strongConnected) {
		if(strongConnected) {
			Integer lowestIndex = 0;
			Map<V, Integer> indexMap = new HashMap<>();
			Map<V, Integer> lowLinkMap = new HashMap<>();
			Stack<V> stack = new Stack<>();
			Set<Set<V>> res = new ListSet<>();
			
			for(V vertex : this.vertexSet()) {
				if(!indexMap.containsKey(vertex)) {
					res.addAll(this.getConnectedComponents(vertex, indexMap, lowLinkMap, stack, lowestIndex));
				}
			}
			
			return res;
		} else {
			AdjacencyListGraph<V,E> aux = this.getSimilarType();
			
			for(V vertex : this.vertexSet()) {
				@SuppressWarnings("unchecked")
				V copy = (V) vertex.copy();
				aux.addVertex(copy);
			}
			
			for(E edge : this.edgeSet()) {
				aux.addEdge(edge.getSource(), edge.getTarget());
				
				if(!this.containsEdge(edge.getTarget(), edge.getSource())) {
					aux.addEdge(edge.getTarget(), edge.getSource());
				}
			}
			
			
			// Is it possible return directly this set?
			Set<Set<V>> partialRes = aux.getConnectedComponents(true);
			
			Set<Set<V>> res = new ListSet<>();
			for(Set<V> component : partialRes) {
				Set<V> finalComp = new ListSet<>();
				
				for(V vertex : component) {
					for(V real : this.vertexSet()) {
						if(real.equals(vertex)) {
							finalComp.add(real);
							break;
						}
					}
				}
				
				res.add(finalComp);
			}
			
			return res;
		}
	}
	
	/**
	 * Private method implementing the Tarjan Algorithm.
	 * @param vertex Current vertex.
	 * @param indexMap Map needed
	 * @param lowLinkMap Another map needed
	 * @param stack A stack needed
	 * @param lowestIndex An integer needed
	 * 
	 * @return Connected Componnents of the Graph.
	 */
	private Set<Set<V>> getConnectedComponents(V vertex, Map<V, Integer> indexMap, Map<V, Integer> lowLinkMap, Stack<V> stack, Integer lowestIndex) {
		// Set the depth index for v to the smallest unused index
		indexMap.put(vertex, lowestIndex);
		lowLinkMap.put(vertex, lowestIndex);
		lowestIndex++;
		
		stack.push(vertex);
		
		Set<Set<V>> res = new ListSet<>();
		
		// Consider succesors of vertex
		for(V target : this.vertexFrom(vertex)) {
			if(!indexMap.containsKey(target)) {
				res.addAll(this.getConnectedComponents(target, indexMap, lowLinkMap, stack, lowestIndex));
		        // Successor w has not yet been visited; recurse on it
				lowLinkMap.put(vertex, Integer.min(lowLinkMap.get(vertex), lowLinkMap.get(target)));
			} else if(stack.contains(target)) {
		        // Successor w is in stack S and hence in the current SCC
				lowLinkMap.put(vertex, Integer.min(lowLinkMap.get(vertex), indexMap.get(target)));
			}
		}
		
	    // If v is a root node, pop the stack and generate an SCC
		if(lowLinkMap.get(vertex).equals(indexMap.get(vertex))) {
			Set<V> newComponent = new ListSet<V>();
			
			try{
				V current = stack.pop();
				
				while(!vertex.equals(current) && !stack.isEmpty()) {
					newComponent.add(current);
					
					current = stack.pop();
				}
				
			} catch (EmptyStackException e) {
				//Impossible Exception
				Log.exception(e);
				Log.print("Theorically impossible exception");
			}
			
			newComponent.add(vertex);
			
			res.add(newComponent);
		}
		
		return res;
	}

	//Auxiliar methods
	/**
	 * Method that return a new Graph with the same class of vertices and edges.
	 * 
	 * @return New AdjacencyListGraph with the same vertex and edge class.
	 */
	public AdjacencyListGraph<V,E> getSimilarType() {
		return new AdjacencyListGraph<>(this.edgeClass, this.vertexClass);
	}
}
