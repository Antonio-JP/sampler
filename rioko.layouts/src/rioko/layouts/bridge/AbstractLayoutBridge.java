package rioko.layouts.bridge;

import java.util.Collection;

import rioko.layouts.algorithms.LayoutAlgorithm;
import rioko.layouts.geometry.Point;
import rioko.layouts.geometry.Rectangle;
import rioko.layouts.graphImpl.LayoutGraph;
import rioko.layouts.graphImpl.LayoutVertex;
import rioko.utilities.collections.ArrayMap;

public abstract class AbstractLayoutBridge<T, V extends LayoutVertex> implements LayoutBridge<T, V> {
	
	protected LayoutGraph graph = null;
	private Class<V> layoutVertexClass;
	
	private ArrayMap<T, V> mapBetweenVertices = null;
	
	private boolean ready = false, finished = false;
	
	/* Builders */
	public AbstractLayoutBridge(Class<V> layoutVertexClass) {
		this.layoutVertexClass = layoutVertexClass;
	}
	
	/* Getters & Setters */
	protected V getLayoutVertex(T vertex) {
		return this.mapBetweenVertices.get(vertex);
	}
	
	/* LayoutBridge methods */
	@Override
	public boolean isReady() {
		return this.ready;
	}
	
	@Override
	public void createBridge(Collection<T> previousVertices, Collection<Object> previousEdges) {
		//The layout is no longer finished applied
		this.finished = false;
		
		//Create a new Layout Graph
		this.graph = new LayoutGraph(this.layoutVertexClass);
		this.mapBetweenVertices = new ArrayMap<T,V>();
		
		//Adding the vertices
		for(T vertex : previousVertices) {
			/* Create the new vertex */
			V newVertex = this.parseVertex(vertex);
			this.mapBetweenVertices.put(vertex, newVertex);
			
			/* Add the new vertex to the graph */
			this.graph.addVertex(newVertex);
		}
		
		//Adding the edges
		for(Object obj : previousEdges) {
			this.parseEdge(obj);
		}
		
		//Now the bridge is ready to run the Layout
		this.ready = true;
	}
	
	@Override
	public boolean isFinished() {
		return this.finished;
	}
	
	@Override
	public void applyLayout(LayoutAlgorithm layoutToApply, Rectangle layoutArea) {
		layoutToApply.applyLayout(this.graph, layoutArea);
		
		//Now it is possible get the final positions for the graph
		this.finished = true;
	}
		
	/* Getting position methods */
	@Override
	public double getX(T vertex) {
		return this.mapBetweenVertices.get(vertex).getPosition().getX();
	}
	
	@Override
	public double getY(T vertex) {
		return this.mapBetweenVertices.get(vertex).getPosition().getY();
	}
	
	public Point getPosition(T vertex) {
		return this.mapBetweenVertices.get(vertex).getPosition();
	}
	
	/* Additional methods */
	public abstract V parseVertex(T vertex);
	public abstract boolean parseEdge(Object obj);

}
