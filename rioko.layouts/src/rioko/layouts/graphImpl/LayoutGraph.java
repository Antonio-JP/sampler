package rioko.layouts.graphImpl;

import rioko.grapht.AbstractGraph;

public class LayoutGraph extends AbstractGraph<LayoutVertex, LayoutEdge> {

	public LayoutGraph() {
		super(LayoutEdge.class, LayoutVertex.class);
	}
	
	public LayoutGraph(Class<? extends LayoutVertex> vertexClass) {
		super(LayoutEdge.class, vertexClass);
	}
}
