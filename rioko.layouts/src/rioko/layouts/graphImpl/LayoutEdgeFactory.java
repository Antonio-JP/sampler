package rioko.layouts.graphImpl;

import org.jgrapht.EdgeFactory;

public class LayoutEdgeFactory implements EdgeFactory<LayoutVertex, LayoutEdge> {

	@Override
	public LayoutEdge createEdge(LayoutVertex source, LayoutVertex target) {
		return new LayoutEdge(source, target);
	}

}
