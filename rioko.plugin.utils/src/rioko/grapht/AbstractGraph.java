package rioko.grapht;

import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.ArrayList;

import org.jgrapht.EdgeFactory;
import org.jgrapht.Graph;

import rioko.searchers.breadth.DirectedSearchIterator;
import rioko.utilities.Log;
import rioko.utilities.collections.ArrayMap;
import rioko.utilities.collections.ListSet;

public class AbstractGraph<V extends Vertex,E extends VisibleEdge<V>> implements Graph<V,E> {
	
	private Map<V,ListSet<E>> adyacencyList = new ArrayMap<>();
	
	private boolean loopsAvaible = false;
	
	protected Class<?> edgeClass = null;
	private Class<?> vertexClass = null;
	
	private EdgeFactory<V,E> edgeFactory = null;
	private VertexFactory<V> vertexFactory = null;
	
	//Builders
	public AbstractGraph(Class<?> edgeClass, Class<?> vertexClass) {
		this.edgeClass = edgeClass;
		this.vertexClass = vertexClass;
	}
	
	//Getters & Setters
	protected void setLoopsAvaible(boolean loopsAvaible) {
		this.loopsAvaible = loopsAvaible;
	}
		
	//Graph methods
	@Override
	public E addEdge(V arg0, V arg1) {
		if(!this.loopsAvaible && arg0.equals(arg1)) {
			return null;
		}
		
		E edge = this.getEdge(arg0, arg1);
		
		if(edge == null) {
			edge = this.getEdgeFactory().createEdge(arg0, arg1);
		
			this.adyacencyList.get(arg0).add(edge);
		}
		
		return edge;
	}

	@Deprecated
	@Override
	public boolean addEdge(V arg0, V arg1, E arg2) {
		return false;
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
	public boolean containsEdge(E arg0) {
		return this.containsEdge(this.getEdgeSource(arg0), this.getEdgeTarget(arg0));
	}

	@Override
	public boolean containsEdge(V arg0, V arg1) {
		return this.getEdge(arg0, arg1) != null;
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
	
	@Override 
	public Set<E> edgesOf(V vertex) {
		Set<E> set = this.edgesFrom(vertex);
		set.addAll(this.edgesTo(vertex));
		
		return set; 
	}

	@Override
	public Set<E> getAllEdges(V arg0, V arg1) {
		Set<E> edges = this.edgesOf(arg0);
		Set<E> res = new ListSet<E>();
		
		for(E edge : edges) {
			if(this.getEdgeSource(edge).equals(arg1) || this.getEdgeTarget(edge).equals(arg1)) {
				res.add(edge);
			}
		}
		
		return res;
	}

	@Override
	public E getEdge(V arg0, V arg1) {
		if(!this.vertexSet().contains(arg0) || !this.vertexSet().contains(arg1)) {
			return null;
		}
		
		for(E edge : this.edgesFrom(arg0)) {
			if(edge.getTarget().equals(arg1)) {
				return edge;
			}
		}
		
		return null;
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
	public V getEdgeSource(E arg0) {
		return arg0.getSource();
	}

	@Override
	public V getEdgeTarget(E arg0) {
		return arg0.getTarget();
	}

	@Override
	public double getEdgeWeight(E arg0) {
		return 1.0;
	}

	@Override
	public boolean removeAllEdges(Collection<? extends E> arg0) {
		boolean res = false;
		
		for(E edge : arg0) {
			res |= this.removeEdge(edge);
		}
		
		return res;
	}

	@Override
	public Set<E> removeAllEdges(V arg0, V arg1) {
		Set<E> edges = this.getAllEdges(arg0, arg1);
		
		this.removeAllEdges(edges);
		
		return edges;
	}

	@Override
	public boolean removeAllVertices(Collection<? extends V> arg0) {
		boolean res = false;
		
		for(V vertex : arg0) {
			res |= this.removeVertex(vertex);
		}
		
		return res;
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
	public E removeEdge(V arg0, V arg1) {
		if(!this.containsEdge(arg0, arg1)) {
			return null;
		}
		
		E edge = this.getEdge(arg0, arg1);
		this.removeEdge(edge);
		
		return edge;
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
	
	//Abstract Graph methods
	public Set<E> edgesFrom(V vertex) {
		return this.adyacencyList.get(vertex);
	}

	public Set<V> vertexFrom(V vertex) {
		Set<V> set = new HashSet<V>();
		
		for(E edge : this.edgesFrom(vertex)) {
			set.add(edge.getTarget());
		}
		
		return set;
	}
	
	public Set<E> edgesTo(V vertex) {
		Set<E> set = new HashSet<E>();
		
		for(E edge : this.edgeSet()) {
			if(vertex.equals(edge.getTarget()))
			{
				set.add(edge);
			}
		}
		
		return set;
	}
	
	public Set<V> vertexTo(V vertex) {
		Set<V> set = new HashSet<V>();
		
		for(E edge : this.edgesTo(vertex)) {
			set.add(edge.getSource());
		}
		
		return set;
	}
	
	//Graph Operations
	@SuppressWarnings("unchecked")
	public AbstractGraph<V,E> inducedSubgraph(Collection<V> nodes)
	{
		AbstractGraph<V, E> result = null;

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
	
	public Collection<V> getSubTree(DirectedSearchIterator<V,E> iterator) {
		//Haremos una búesqueda en anchura usando como criterio que las aristas sean de contención
		ArrayList<V> subTreeNodes = new ArrayList<>();
		
		while(iterator.hasNext()) {
			subTreeNodes.add((V)iterator.next());
		}
		
		return subTreeNodes;
	}
	
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
			AbstractGraph<V,E> aux = this.getSimilarType();
			
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
	
	public AbstractGraph<V,E> getSimilarType() {
		return new AbstractGraph<>(this.edgeClass, this.vertexClass);
	}
}
