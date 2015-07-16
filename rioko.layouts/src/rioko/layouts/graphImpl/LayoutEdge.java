package rioko.layouts.graphImpl;

import rioko.grapht.AbstractEdge;

public class LayoutEdge extends AbstractEdge<LayoutVertex> {
	
	public LayoutEdge() {
		super();
	}
	
	public LayoutEdge(LayoutVertex source, LayoutVertex target) {
		super(source, target);
	}
	
	@Override
	public LayoutEdgeFactory getEdgeFactory()
	{
		return new LayoutEdgeFactory();
	}
}
