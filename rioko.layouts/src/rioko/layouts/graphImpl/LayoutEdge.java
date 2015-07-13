package rioko.layouts.graphImpl;

import org.jgrapht.EdgeFactory;

import rioko.grapht.VisibleEdge;

public class LayoutEdge extends VisibleEdge<LayoutVertex> {
	
	public LayoutEdge() {
		super();
	}
	
	public LayoutEdge(LayoutVertex source, LayoutVertex target) {
		super(source, target);
	}
	
	@Override
	public EdgeFactory<LayoutVertex, LayoutEdge> getEdgeFactory()
	{
		return new LayoutEdgeFactory();
	}

	private static final long serialVersionUID = 1277671342583507857L;
}
