package rioko.layouts.graphImpl;

import rioko.grapht.AdjacencyListGraph;

public class LayoutGraph extends AdjacencyListGraph<LayoutVertex, LayoutEdge> {

	public LayoutGraph() {
		super(LayoutEdge.class, LayoutVertex.class);
	}
	
	public LayoutGraph(Class<? extends LayoutVertex> vertexClass) {
		super(LayoutEdge.class, vertexClass);
	}
}
